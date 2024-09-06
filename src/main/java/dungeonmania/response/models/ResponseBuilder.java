package dungeonmania.response.models;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.Game;
import dungeonmania.battles.BattleRound;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Interactable;
import dungeonmania.entities.inventory.Inventory;
import dungeonmania.entities.logical.LightBulb;
import dungeonmania.util.NameConverter;
import dungeonmania.util.Position;

/**
 * DO NOT CHANGE THIS FILE
 */
public class ResponseBuilder {
    public static DungeonResponse getDungeonResponse(Game game) {
        List<EntityResponse> entityResponse = new ArrayList<>();
        game.getMap().getEntities().forEach(e -> {
            entityResponse.add(ResponseBuilder.getEntityResponse(game, e));
        });
        return new DungeonResponse(game.getId(), game.getName(), entityResponse,
                (game.getPlayer() != null) ? getInventoryResponse(game.getPlayer().getInventory()) : null,
                game.getBattleFacade().getBattleResponses(),
                (game.getPlayer() != null) ? game.getPlayer().getBuildables() : null,
                (game.getGoals().achieved(game)) ? "" : game.getGoals().toString(game));
    }

    private static List<ItemResponse> getInventoryResponse(Inventory inventory) {
        return inventory.getEntities().stream().map(ResponseBuilder::getItemResponse).collect(Collectors.toList());
    }

    public static ItemResponse getItemResponse(Entity entity) {
        return new ItemResponse(entity.getId(), NameConverter.toSnakeCase(entity));
    }

    public static EntityResponse getEntityResponse(Game game, Entity entity) {
        String entityId = entity.getId();
        String entityImage;
        if (entity instanceof LightBulb) {
            LightBulb lightBulb = (LightBulb) entity;
            entityImage = lightBulb.isActivated() ? "light_bulb_on" : "light_bulb_off";
        } else {
            entityImage = NameConverter.toSnakeCase(entity);
        }
        Position entityPosition = entity.getPosition();
        boolean isInteractable = (entity instanceof Interactable)
                && ((Interactable) entity).isInteractable(game.getPlayer());

        return new EntityResponse(entityId, entityImage, entityPosition, isInteractable);
    }

    public static RoundResponse getRoundResponse(BattleRound round) {
        return new RoundResponse(round.getDeltaSelfHealth(), round.getDeltaTargetHealth());
    }
}
