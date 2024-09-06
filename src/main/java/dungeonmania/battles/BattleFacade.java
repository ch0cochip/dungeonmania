package dungeonmania.battles;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.Game;
import dungeonmania.entities.BattleItem;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Player;
import dungeonmania.entities.collectables.potions.Potion;
import dungeonmania.entities.enemies.Enemy;
import dungeonmania.entities.enemies.Mercenary;
import dungeonmania.entities.inventory.InventoryItem;
import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.ResponseBuilder;
import dungeonmania.util.NameConverter;

public class BattleFacade {
    private List<BattleResponse> battleResponses = new ArrayList<>();

    public void battle(Game game, Player player, Enemy enemy) {
        final int DEFAULT_HEALTH = 0;
        final int DEFAULT_ATTACK = 0;
        final int DEFAULT_DEFENCE = 0;
        final int DEFAULT_ATTACK_MAGNIFIER = 1;
        final int DEFAULT_DAMAGE_REDUCER = 1;
        // 0. init
        double initialPlayerHealth = player.getBattleStatisticsHealth();
        double initialEnemyHealth = enemy.getBattleStatisticsHealth();
        String enemyString = NameConverter.toSnakeCase(enemy);

        // 1. apply buff provided by the game and player's inventory
        // getting buffing amount
        List<BattleItem> battleItems = new ArrayList<>();
        BattleStatistics playerBuff = new BattleStatistics(DEFAULT_HEALTH, DEFAULT_ATTACK, DEFAULT_DEFENCE,
                DEFAULT_ATTACK_MAGNIFIER, DEFAULT_DAMAGE_REDUCER);

        Potion effectivePotion = player.getEffectivePotion();
        if (effectivePotion != null) {
            playerBuff = player.applyBuff(playerBuff);
        } else {
            for (BattleItem item : player.getBattleItemInInventory(BattleItem.class)) {
                if (item instanceof Potion)
                    continue;
                playerBuff = item.applyBuff(playerBuff);
                battleItems.add(item);
            }
        }

        List<Mercenary> mercs = game.getMercenaryFromMap();
        for (Mercenary merc : mercs) {
            if (!merc.isAllied())
                continue;
            playerBuff = BattleStatistics.applyBuff(playerBuff, merc.getBattleStatistics());
        }

        // 2. Battle the two stats
        BattleStatistics playerBaseStatistics = player.getBattleStatistics();
        BattleStatistics enemyBaseStatistics = enemy.getBattleStatistics();
        BattleStatistics playerBattleStatistics = BattleStatistics.applyBuff(playerBaseStatistics, playerBuff);
        BattleStatistics enemyBattleStatistics = enemyBaseStatistics;
        if (!playerBattleStatistics.isEnabled() || !enemyBaseStatistics.isEnabled())
            return;
        List<BattleRound> rounds = BattleStatistics.battle(playerBattleStatistics, enemyBattleStatistics);

        // 3. update health to the actual statistics
        player.setPlayerHealthToActualStatistics(playerBattleStatistics.getHealth());
        enemy.setEnemyHealthToActualStatistics(enemyBattleStatistics.getHealth());

        // 4. call to decrease durability of items
        for (BattleItem item : battleItems) {
            if (item instanceof InventoryItem)
                item.use(game);
        }

        // 5. Log the battle - solidate it to be a battle response
        battleResponses.add(new BattleResponse(enemyString,
                rounds.stream().map(ResponseBuilder::getRoundResponse).collect(Collectors.toList()),
                battleItems.stream().map(Entity.class::cast).map(ResponseBuilder::getItemResponse)
                        .collect(Collectors.toList()),
                initialPlayerHealth, initialEnemyHealth));
    }

    public List<BattleResponse> getBattleResponses() {
        return battleResponses;
    }
}
