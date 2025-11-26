package com.iafenvoy.minedash.render.util;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.Vector3f;

import java.util.LinkedList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public final class VertexCollector {
    private final List<Vector3f> vertexes = new LinkedList<>();

    public void add(float x, float y, float z) {
        this.vertexes.add(new Vector3f(x, y, z));
    }

    public void forEach(VertexResolver resolver) {
        this.vertexes.forEach(p -> resolver.accept(p.x, p.y, p.z));
    }

    @FunctionalInterface
    public interface VertexResolver {
        void accept(float x, float y, float z);
    }
}
