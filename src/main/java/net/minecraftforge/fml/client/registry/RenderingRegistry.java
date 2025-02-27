/*
 * Minecraft Forge
 * Copyright (c) 2016-2019.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation version 2.1
 * of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package net.minecraftforge.fml.client.registry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.Entity;

public class RenderingRegistry
{
    private static final RenderingRegistry INSTANCE = new RenderingRegistry();

    private final Map<Class<? extends Entity>, IRenderFactory<? extends Entity>> entityRenderers = new ConcurrentHashMap<>();

    /**
     * Register an entity rendering handler. This will, after mod initialization, be inserted into the main
     * render map for entities.
     * Call this during {@link net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent}.
     * This method is safe to call during parallel mod loading.
     */
    public static <T extends Entity> void registerEntityRenderingHandler(Class<T> entityClass, IRenderFactory<? super T> renderFactory)
    {
        INSTANCE.entityRenderers.put(entityClass, renderFactory);
    }

    public static void loadEntityRenderers(EntityRendererManager manager, Map<Class<? extends Entity>, EntityRenderer<? extends Entity>> renderMap)
    {
        INSTANCE.entityRenderers.forEach((key, value) -> register(manager, renderMap, key, value));
    }

    @SuppressWarnings("unchecked")
    private static <T extends Entity> void register(EntityRendererManager manager, Map<Class<? extends Entity>, EntityRenderer<? extends Entity>> renderMap, Class<T> entityClass, IRenderFactory<?> renderFactory)
    {
        renderMap.put(entityClass, ((IRenderFactory<T>)renderFactory).createRenderFor(manager));
    }
}
