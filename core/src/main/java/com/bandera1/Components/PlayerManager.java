package com.bandera1.Components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;
import com.bandera1.Engine.GameObjects.*;
import com.bandera1.Engine.Systems.SceneSystem;
import com.bandera1.Utils.ServerMessage;
import com.bandera1.Utils.ServerUtils;
import com.bandera1.Utils.WebSocketEventListener;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PlayerManager extends Component implements WebSocketEventListener {

    private GameObject player;
    private Map<String, GameObject> otherPlayers;
    private TextRenderer textRenderer;
    private boolean active;
    private PlayerAnimator.Direction lastDirection = PlayerAnimator.Direction.DOWN;

    public static boolean attackRequested = false;
    private boolean isAttacking = false;
    private float attackTimer = 0f;
    private final float attackAnimDuration = 0.3f;
    private float attackCooldown = 0f;
    private final float attackCooldownTime = 0.5f;
    private final float attackRange = 64f;

    @Override
    public void init() {
        ServerUtils.instance.addListener(this);
        otherPlayers = new HashMap<>();
    }

    @Override
    public void start() {
        active = true;
        player = GameObject.Find("player");
        textRenderer = GameObject.Find("playerPointsText").getComponent(TextRenderer.class);

        GameObject attackButtonObj = new GameObject("attackButton");
        attackButtonObj.addComponent(new AttackButton());
        SceneSystem.activeScene.addGameObject(attackButtonObj);
    }

    @Override
    public void onConnect() {}

    @Override
    public void onDisconnect(int closeCode, String reason) {
        for (GameObject p : otherPlayers.values()) {
            GameObject.Destroy(p);
        }
        otherPlayers.clear();
    }

    @Override
    public void onMessage(ServerMessage message) {
        if (!active) return;

        if (message.type.equals("update")) {
            JsonValue data = message.data;

            if (data.has("clientPlayer")) {
                JsonValue clientPlayerData = data.get("clientPlayer");
                if (player.getComponent(AnimationRenderer.class) == null) {
                    createAnimationRenderer(player, clientPlayerData);
                }
                updatePlayer(player, clientPlayerData, true);
            }

            if (data.has("otherPlayers")) {
                updateOtherPlayers(data.get("otherPlayers"));
            }
        }
    }

    private void updatePlayer(GameObject player, JsonValue playerData, boolean isLocal) {
        if (player == null || playerData == null) return;

        float x = playerData.getFloat("x");
        float y = playerData.getFloat("y");
        float dx = playerData.get("moveVector").getFloat("dx");
        float dy = playerData.get("moveVector").getFloat("dy");
        float speedX = playerData.get("speedVector").getFloat("speedX");
        float speedY = playerData.get("speedVector").getFloat("speedY");
        String nickname = playerData.getString("nickname", isLocal ? "You" : "Player");
        int teamId = playerData.getInt("teamId", 0);
        int skinId = playerData.getInt("skinId", 1);
        boolean hasKey = playerData.getBoolean("hasKey", false);
        boolean hasFlag = playerData.getBoolean("hasFlag", false);

        PositionSync positionSync = player.getComponent(PositionSync.class);
        if (positionSync != null) {
            positionSync.targetPosition.set(x, y);
            positionSync.velocity.set(speedX, speedY);
            positionSync.snap = (speedX == 0 && speedY == 0);
        }

        Player playerComp = player.getComponent(Player.class);
        if (playerComp != null) {
            if (playerComp.teamId != teamId) {
                playerComp.teamId = teamId;
                ensureTextRenderers(player, nickname, teamId);
            }
            if (playerComp.skinId != skinId || playerComp.hasKey != hasKey || playerComp.hasFlag != hasFlag) {
                playerComp.skinId = skinId;
                playerComp.hasKey = hasKey;
                playerComp.hasFlag = hasFlag;
                updateAnimationRenderer(player, skinId, hasKey, hasFlag);
            }
        }

        if (isLocal && textRenderer != null && playerData.has("points")) {
            int points = playerData.getInt("points");
            textRenderer.setText("Points: " + points);
        }

        AnimationRenderer renderer = player.getComponent(AnimationRenderer.class);
        if (renderer != null) {
            float delta = Gdx.graphics.getDeltaTime();

            if (attackCooldown > 0f) attackCooldown -= delta;

            if (isAttacking) {
                attackTimer -= delta;
                if (attackTimer <= 0f) {
                    isAttacking = false;
                }
            }

            if (!isAttacking) {
                if (dx == 0 && dy == 0) {
                    renderer.play("IDLE_" + lastDirection.name());
                } else {
                    lastDirection = Math.abs(dx) > Math.abs(dy)
                            ? (dx > 0 ? PlayerAnimator.Direction.RIGHT : PlayerAnimator.Direction.LEFT)
                            : (dy > 0 ? PlayerAnimator.Direction.UP : PlayerAnimator.Direction.DOWN);
                    renderer.play("WALK_" + lastDirection.name());
                }
            }

            if (isLocal && attackRequested && attackCooldown <= 0f && !isAttacking) {
                attackRequested = false;
                if (playerComp == null /* || playerComp.isDead */) return;

                renderer.play("ATTACK_" + lastDirection.name());
                isAttacking = true;
                attackTimer = attackAnimDuration;
                attackCooldown = attackCooldownTime;

                checkForHit();
            }
        }
    }

    private void updateOtherPlayers(JsonValue array) {
        Set<String> currentIds = new HashSet<>();
        for (JsonValue data : array) {
            String id = data.getString("id");
            currentIds.add(id);
            GameObject obj = otherPlayers.get(id);
            if (obj == null) {
                obj = createNewOtherPlayer(data);
                otherPlayers.put(id, obj);
            } else {
                updatePlayer(obj, data, false);
            }
        }

        otherPlayers.entrySet().removeIf(entry -> {
            if (!currentIds.contains(entry.getKey())) {
                GameObject.Destroy(entry.getValue());
                return true;
            }
            return false;
        });
    }

    private GameObject createNewOtherPlayer(JsonValue data) {
        String id = data.getString("id");
        float x = data.getFloat("x");
        float y = data.getFloat("y");
        int skinId = data.getInt("skinId", 1);
        boolean hasKey = data.getBoolean("hasKey", false);
        boolean hasFlag = data.getBoolean("hasFlag", false);
        int teamId = data.getInt("teamId", 0);
        String nickname = data.getString("nickname", "Player");

        GameObject obj = new GameObject("player " + id);

        PlayerAnimator animator = createAnimatorForSkin(skinId, hasKey, hasFlag);
        AnimationRenderer renderer = new AnimationRenderer(animator.getAnimation(PlayerAnimator.Action.IDLE, PlayerAnimator.Direction.DOWN).getKeyFrame(0));

        for (PlayerAnimator.Action act : PlayerAnimator.Action.values()) {
            for (PlayerAnimator.Direction dir : PlayerAnimator.Direction.values()) {
                renderer.addAnimation(act.name() + "_" + dir.name(), animator.getAnimation(act, dir));
            }
        }
        renderer.play("IDLE_DOWN");

        obj.addComponent(new PositionSync());
        obj.addComponent(renderer);
        obj.addComponent(new Player(id, skinId, hasKey, hasFlag, teamId));
        obj.transform.position.set(x, y);
        obj.transform.scale.set(4f, 4f);

        ensureTextRenderers(obj, nickname, teamId);
        SceneSystem.activeScene.addGameObject(obj);
        return obj;
    }

    private void ensureTextRenderers(GameObject player, String nickname, int teamId) {
        TextRenderer teamText = player.getComponent(TextRenderer.class);
        if (teamText == null) {
            teamText = new TextRenderer(getTeamName(teamId));
            teamText.offsetY = 70f;
            teamText.fontScale = 0.3f;
            player.addComponent(teamText);
        } else {
            teamText.setText(getTeamName(teamId));
        }

        TextRenderer nameText = player.getComponent(TextRenderer.class);
        if (nameText == null) {
            nameText = new TextRenderer(nickname);
            nameText.offsetY = 40f;
            nameText.fontScale = 0.3f;
            player.addComponent(nameText);
        } else {
            nameText.setText(nickname);
        }
    }

    private void checkForHit() {
        Vector2 origin = new Vector2(player.transform.position);
        Vector2 dir = new Vector2(0, 0);
        switch (lastDirection) {
            case UP:
                dir.y = 1;
                break;
            case DOWN:
                dir.y = -1;
                break;
            case LEFT:
                dir.x = -1;
                break;
            case RIGHT:
                dir.x = 1;
                break;
        }

        Vector2 attackPos = origin.add(dir.scl(attackRange));

        for (GameObject enemy : otherPlayers.values()) {
            Player enemyComp = enemy.getComponent(Player.class);
            if (enemyComp == null || enemyComp.teamId == player.getComponent(Player.class).teamId) continue;

            float dist = enemy.transform.position.dst(attackPos);
            if (dist < attackRange) {
                Gdx.app.log("ATTACK", "Golpeaste a " + enemy.getName());
                // TODO: enviar mensaje al servidor
            }
        }
    }

    private PlayerAnimator createAnimatorForSkin(int skinId, boolean hasKey, boolean hasFlag) {
        String path = "Characters/Character" + skinId + "/";
        String walk = hasFlag ? "Char_Carry_Flag_Walk.png" : hasKey ? "Char_Carry_Key_Walk.png" : "Char_Walk.png";
        String idle = hasFlag ? "Char_Carry_Flag_Idle.png" : hasKey ? "Char_Carry_Key_Idle.png" : "Char_Idle.png";
        return new PlayerAnimator(path, walk, idle, "Char_Attack.png", "Char_Death.png");
    }

    private void updateAnimationRenderer(GameObject player, int skinId, boolean hasKey, boolean hasFlag) {
        AnimationRenderer renderer = player.getComponent(AnimationRenderer.class);
        if (renderer == null) return;

        PlayerAnimator animator = createAnimatorForSkin(skinId, hasKey, hasFlag);
        renderer.clearAnimations();

        for (PlayerAnimator.Action act : PlayerAnimator.Action.values()) {
            for (PlayerAnimator.Direction dir : PlayerAnimator.Direction.values()) {
                renderer.addAnimation(act.name() + "_" + dir.name(), animator.getAnimation(act, dir));
            }
        }
        renderer.play("IDLE_" + lastDirection.name());
    }

    private void createAnimationRenderer(GameObject player, JsonValue data) {
        int skinId = data.getInt("skinId", 1);
        boolean hasKey = data.getBoolean("hasKey", false);
        boolean hasFlag = data.getBoolean("hasFlag", false);

        PlayerAnimator animator = createAnimatorForSkin(skinId, hasKey, hasFlag);
        AnimationRenderer renderer = new AnimationRenderer(animator.getAnimation(PlayerAnimator.Action.IDLE, PlayerAnimator.Direction.DOWN).getKeyFrame(0));

        for (PlayerAnimator.Action act : PlayerAnimator.Action.values()) {
            for (PlayerAnimator.Direction dir : PlayerAnimator.Direction.values()) {
                renderer.addAnimation(act.name() + "_" + dir.name(), animator.getAnimation(act, dir));
            }
        }
        renderer.play("IDLE_DOWN");

        player.addComponent(renderer);
        player.addComponent(new Player(player.getName(), skinId, hasKey, hasFlag, 0));
    }

    private String getTeamName(int teamId) {
        switch (teamId) {
            case 0: return "Lornwood";
            case 1: return "Vileswamp";
            case 2: return "Asharid";
            case 3: return "Ironhold";
            default: return "Unknown";
        }
    }

    public static void requestAttack() {
        attackRequested = true;
    }

    @Override
    public void onBinaryMessage(byte[] data) {}

    @Override
    public void onError(Throwable error) {
        Gdx.app.log("PlayerManager", "WebSocket error: " + error.getMessage());
    }
}
