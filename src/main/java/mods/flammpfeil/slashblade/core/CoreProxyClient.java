package mods.flammpfeil.slashblade.core;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import mods.flammpfeil.slashblade.*;
import mods.flammpfeil.slashblade.client.model.BladeModelManager;
import mods.flammpfeil.slashblade.client.renderer.entity.*;
import mods.flammpfeil.slashblade.client.renderer.entity.layers.LayerSlashBlade;
import mods.flammpfeil.slashblade.event.ModelRegister;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.network.NetworkManager;
import mods.flammpfeil.slashblade.stats.AchievementList;
import mods.flammpfeil.slashblade.util.KeyBindingEx;
import mods.flammpfeil.slashblade.util.ReflectionAccessHelper;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.Items;
import net.minecraft.util.*;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import mods.flammpfeil.slashblade.ability.AvoidAction;
import mods.flammpfeil.slashblade.ability.client.StylishRankRenderer;
import mods.flammpfeil.slashblade.entity.*;
import mods.flammpfeil.slashblade.gui.AchievementsExtendedGuiHandler;
import mods.flammpfeil.slashblade.network.MessageSpecialAction;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.lwjgl.input.Keyboard;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;

public class CoreProxyClient extends CoreProxy {

	@Override
	public void initializeItemRenderer() {
        //resource reload event
        MinecraftForge.EVENT_BUS.register(BladeModelManager.getInstance());

        new ModelRegister();

        MinecraftForge.EVENT_BUS.register(new BladeFirstPersonRender());

		/*
        MinecraftForgeClient.registerItemRenderer(SlashBlade.weapon, renderer);
		MinecraftForgeClient.registerItemRenderer(SlashBlade.bladeWood, renderer);
		MinecraftForgeClient.registerItemRenderer(SlashBlade.bladeBambooLight, renderer);
		MinecraftForgeClient.registerItemRenderer(SlashBlade.bladeSilverBambooLight, renderer);
        MinecraftForgeClient.registerItemRenderer(SlashBlade.bladeWhiteSheath, renderer);
        MinecraftForgeClient.registerItemRenderer(SlashBlade.wrapBlade, renderer);
        MinecraftForgeClient.registerItemRenderer(SlashBlade.bladeNamed, renderer);
        */

        /*
        {
            List<ResourceLocation> variants = Lists.newArrayList();
            variants.add(new ModelResourceLocation(SlashBlade.modid + ":" + "proudsoul", "inventory"));
            variants.add(new ModelResourceLocation(Item.itemRegistry.getNameForObject(Items.iron_ingot).toString()));
            variants.add(new ModelResourceLocation(Item.itemRegistry.getNameForObject(Items.snowball).toString()));
            variants.add(new ModelResourceLocation(SlashBlade.modid + ":" + "tinyps", "inventory"));

            for(Map.Entry<String, Integer> entry : AchievementList.achievementIcons.entrySet()) {
                variants.add(new ModelResourceLocation(SlashBlade.modid + ":" + entry.getKey(), "inventory"));
            }

            ModelBakery.registerItemVariants(SlashBlade.proudSoul, variants.toArray(new ResourceLocation[]{}));
        }
        */
        ModelLoader.setCustomModelResourceLocation(SlashBlade.proudSoul, 0, new ModelResourceLocation(SlashBlade.modid + ":" + "proudsoul"));
        ModelLoader.setCustomModelResourceLocation(SlashBlade.proudSoul, 1, new ModelResourceLocation(((ResourceLocation)Item.itemRegistry.getNameForObject(Items.iron_ingot)).toString()));
        ModelLoader.setCustomModelResourceLocation(SlashBlade.proudSoul, 2, new ModelResourceLocation(((ResourceLocation)Item.itemRegistry.getNameForObject(Items.snowball)).toString()));
        ModelLoader.setCustomModelResourceLocation(SlashBlade.proudSoul, 3, new ModelResourceLocation(SlashBlade.modid + ":" + "tinyps"));
        for(Map.Entry<String, Integer> entry : AchievementList.achievementIcons.entrySet()) {
            ModelLoader.setCustomModelResourceLocation(SlashBlade.proudSoul, entry.getValue(), new ModelResourceLocation(SlashBlade.modid + ":" + entry.getKey()));
        }


        MinecraftForge.EVENT_BUS.register(new StylishRankRenderer());

        RenderingRegistry.registerEntityRenderingHandler(EntityDrive.class, new IRenderFactory<EntityDrive>() {
            @Override
            public Render<? super EntityDrive> createRenderFor(RenderManager manager) {
                return new RenderDrive(manager);
            }
        });

        RenderingRegistry.registerEntityRenderingHandler(EntitySummonedSword.class, new IRenderFactory<EntitySummonedSword>() {
            @Override
            public Render<? super EntitySummonedSword> createRenderFor(RenderManager manager) {
                return new RenderPhantomSword(manager);
            }
        });


        RenderingRegistry.registerEntityRenderingHandler(EntitySpearManager.class, new IRenderFactory<EntitySpearManager>() {
            @Override
            public Render<? super EntitySpearManager> createRenderFor(RenderManager manager) {
                return null;
            }
        });
        RenderingRegistry.registerEntityRenderingHandler(EntityJudgmentCutManager.class, new IRenderFactory<EntityJudgmentCutManager>() {
            @Override
            public Render<? super EntityJudgmentCutManager> createRenderFor(RenderManager manager) {
                return null;
            }
        });
        RenderingRegistry.registerEntityRenderingHandler(EntitySakuraEndManager.class, new IRenderFactory<EntitySakuraEndManager>() {
            @Override
            public Render<? super EntitySakuraEndManager> createRenderFor(RenderManager manager) {
                return null;
            }
        });
        RenderingRegistry.registerEntityRenderingHandler(EntityJustGuardManager.class, new IRenderFactory<EntityJustGuardManager>() {
            @Override
            public Render<? super EntityJustGuardManager> createRenderFor(RenderManager manager) {
                return null;
            }
        });


        RenderingRegistry.registerEntityRenderingHandler(EntitySummonedSwordBase.class, new IRenderFactory<EntitySummonedSwordBase>() {
            @Override
            public Render<? super EntitySummonedSwordBase> createRenderFor(RenderManager manager) {
                return new RenderPhantomSwordBase(manager);
            }
        });
        RenderingRegistry.registerEntityRenderingHandler(EntityBladeStand.class, new IRenderFactory<EntityBladeStand>() {
            @Override
            public Render<? super EntityBladeStand> createRenderFor(RenderManager manager) {
                return new BladeStandRender(manager);
            }
        });


        KeyBinding keybind = new KeyBindingEx("Key.SlashBlade.PS",-98,"flammpfeil.slashblade"){
            @Override
            public void upkey(int count) {
                Minecraft mc = Minecraft.getMinecraft();
                EntityPlayerSP player = mc.thePlayer;
                if(player != null && !mc.isGamePaused() && mc.inGameHasFocus && mc.currentScreen == null){
                    ItemStack item = player.getHeldItem();
                    if(item != null && item.getItem() instanceof ItemSlashBlade){

                        mc.playerController.updateController();

                        ((ItemSlashBlade)item.getItem()).doRangeAttack(item, player, 1);
                    }
                }
            }
        };

        KeyBinding keybind2 = new KeyBindingEx("Key.SlashBlade.SA", Keyboard.KEY_V,"flammpfeil.slashblade"){
            @Override
            public void downkey() {
                Minecraft mc = Minecraft.getMinecraft();
                EntityPlayerSP player = mc.thePlayer;
                if(player == null) return;
                if(mc.isGamePaused()) return;
                if(!mc.inGameHasFocus) return;
                if(mc.currentScreen != null) return;

                ItemStack item = player.getHeldItem();
                if(item == null) return;
                if(!(item.getItem() instanceof ItemSlashBlade)) return;

                if(GameSettings.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindForward)){

                    mc.playerController.updateController();
                    NetworkManager.INSTANCE.sendToServer(new MessageSpecialAction((byte) 1));

                }else
                if(mc.thePlayer.moveStrafing != 0.0f || mc.thePlayer.moveForward != 0.0f){
                    AvoidAction.doAvoid();
                }
            }

            @Override
            public void presskey(int count) {
                super.presskey(count);

                Minecraft mc = Minecraft.getMinecraft();
                EntityPlayerSP player = mc.thePlayer;
                if(player == null) return;
                if(mc.isGamePaused()) return;
                if(!mc.inGameHasFocus) return;
                if(mc.currentScreen != null) return;

                ItemStack item = player.getHeldItem();
                if(item == null) return;
                if(!(item.getItem() instanceof ItemSlashBlade)) return;

                ItemSlashBlade bladeItem = (ItemSlashBlade)item.getItem();

                EnumSet<ItemSlashBlade.SwordType> types = bladeItem.getSwordType(item);

                if(!types.containsAll(ItemSlashBlade.SwordType.BewitchedPerfect)) return;
                if(!types.contains(ItemSlashBlade.SwordType.FiercerEdge)) return;


                player.worldObj.spawnParticle(EnumParticleTypes.PORTAL,
                        player.posX + (player.getRNG().nextDouble() - 0.5D) * (double)player.width,
                        player.posY + player.getRNG().nextDouble() * (double)player.height - 0.25D,
                        player.posZ + (player.getRNG().nextDouble() - 0.5D) * (double)player.width,
                        (player.getRNG().nextDouble() - 0.5D) * 2.0D, -player.getRNG().nextDouble(), (player.getRNG().nextDouble() - 0.5D) * 2.0D);
            }

            @Override
            public void upkey(int count) {
                super.upkey(count);

                Minecraft mc = Minecraft.getMinecraft();
                EntityPlayerSP player = mc.thePlayer;
                if(player == null) return;
                if(mc.isGamePaused()) return;
                if(!mc.inGameHasFocus) return;
                if(mc.currentScreen != null) return;

                ItemStack item = player.getHeldItem();
                if(item == null) return;
                if(!(item.getItem() instanceof ItemSlashBlade)) return;

                ItemSlashBlade bladeItem = (ItemSlashBlade)item.getItem();

                EnumSet<ItemSlashBlade.SwordType> types = bladeItem.getSwordType(item);

                if(!types.containsAll(ItemSlashBlade.SwordType.BewitchedPerfect)) return;
                if(!types.contains(ItemSlashBlade.SwordType.FiercerEdge)) return;

                if(20 > count) return;

                mc.playerController.updateController();
                NetworkManager.INSTANCE.sendToServer(new MessageSpecialAction((byte) 3));


            }
        };

        AchievementsExtendedGuiHandler extendedGuiHandler = new AchievementsExtendedGuiHandler();
	}


    @Override
    public void postInit() {
        RenderManager rm = Minecraft.getMinecraft().getRenderManager();

        for(Render render : Iterables.concat(rm.getSkinMap().values(), rm.entityRenderMap.values())){
            if(!(render instanceof RendererLivingEntity))
                continue;

            RendererLivingEntity rle = (RendererLivingEntity) render;


            if(!(rle.getMainModel() instanceof ModelBiped))
                continue;

            if(rle instanceof RenderZombie){
                List<LayerRenderer> layers = ReflectionHelper.getPrivateValue(RenderZombie.class, (RenderZombie)rle, "field_177121_n");
                layers.add(new LayerSlashBlade(rle));

                layers = ReflectionHelper.getPrivateValue(RenderZombie.class, (RenderZombie)rle,"field_177122_o");
                layers.add(new LayerSlashBlade(rle));
            }

            rle.addLayer(new LayerSlashBlade(rle));
        }
    }

    /**
     * @param len max 6.0
     */
    @Override
    public void getMouseOver(double len)
    {
        Minecraft mc = Minecraft.getMinecraft();

        float partialTicks = ReflectionAccessHelper.getPartialTicks();

        EntityRenderer er = mc.entityRenderer;
        Entity entity = mc.getRenderViewEntity();
        if (entity != null)
        {
            if (mc.theWorld != null)
            {
                mc.pointedEntity = null;
                double d0 = len;;
                mc.objectMouseOver = entity.rayTrace(d0, partialTicks);
                double d1 = d0;
                Vec3 vec3 = entity.getPositionEyes(partialTicks);


                if (mc.objectMouseOver != null)
                {
                    d1 = mc.objectMouseOver.hitVec.distanceTo(vec3);
                }

                Vec3 vec31 = entity.getLook(partialTicks);
                Vec3 vec32 = vec3.addVector(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0);
                Entity pointedEntity = null;
                Vec3 vec33 = null;
                float f = 1.0F;
                List<Entity> list = mc.theWorld.getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().addCoord(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0).expand((double) f, (double) f, (double) f), Predicates.and(EntitySelectors.NOT_SPECTATING, new Predicate<Entity>() {
                    public boolean apply(Entity p_apply_1_) {
                        return p_apply_1_.canBeCollidedWith();
                    }
                }));
                double d2 = d1;

                for (int i = 0; i < list.size(); ++i)
                {
                    Entity entity1 = (Entity)list.get(i);

                    if (entity1.canBeCollidedWith())
                    {
                        float f2 = entity1.getCollisionBorderSize();
                        AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expand((double) f2, (double) f2, (double)f2);
                        MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3, vec32);

                        if (axisalignedbb.isVecInside(vec3))
                        {
                            if (0.0D <= d2)
                            {
                                pointedEntity = entity1;
                                vec33 = movingobjectposition == null ? vec3 : movingobjectposition.hitVec;
                                d2 = 0.0D;
                            }
                        }
                        else if (movingobjectposition != null)
                        {
                            double d3 = vec3.distanceTo(movingobjectposition.hitVec);

                            if (d3 < d2 || d2 == 0.0D)
                            {
                                if (entity1 == entity1.ridingEntity && !entity1.canRiderInteract())
                                {
                                    if (d2 == 0.0D)
                                    {
                                        pointedEntity = entity1;
                                        vec33 = movingobjectposition.hitVec;
                                    }
                                }
                                else
                                {
                                    pointedEntity = entity1;
                                    vec33 = movingobjectposition.hitVec;
                                    d2 = d3;
                                }
                            }
                        }
                    }
                }

                if (pointedEntity != null && (d2 < d1 || mc.objectMouseOver == null))
                {
                    mc.objectMouseOver = new MovingObjectPosition(pointedEntity, vec33);

                    if (pointedEntity instanceof EntityLivingBase || pointedEntity instanceof EntityItemFrame)
                    {
                        mc.pointedEntity = pointedEntity;
                    }
                }
            }
        }
    }
}
