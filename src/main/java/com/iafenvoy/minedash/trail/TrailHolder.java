package com.iafenvoy.minedash.trail;

import com.iafenvoy.minedash.util.MDMath;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TrailHolder {
    private final PointHolder vertical = new PointHolder(), horizontal = new PointHolder();
    private final float width, length;

    public TrailHolder(float width, int maxLength) {
        this.width = width;
        this.length = maxLength;
    }

    public List<TrailPoint> getVerticalPoints() {
        return this.vertical.renderPoints;
    }

    public List<TrailPoint> getHorizontalPoints() {
        return this.horizontal.renderPoints;
    }

    public void tick(Vec3 pos, Vec3 delta) {
        float yaw = MDMath.positionToYaw(delta);
        float pitch = MDMath.positionToPitch(delta);
        Vec3 upper = MDMath.rotationToPosition(pos, this.width / 2f, pitch - 90, yaw);
        Vec3 lower = MDMath.rotationToPosition(pos, this.width / 2f, pitch + 90, yaw);
        Vec3 offset = upper.subtract(lower).cross(delta).normalize().scale(this.width / 2);
        Vec3 upper1 = pos.add(offset);
        Vec3 lower1 = pos.add(offset.scale(-1));
        this.vertical.addPoint(new TrailPoint(upper, lower));
        this.vertical.update();
        this.horizontal.addPoint(new TrailPoint(upper1, lower1));
        this.horizontal.update();
    }

    private class PointHolder {
        private final List<TrailPoint> actualPoints = new LinkedList<>(), renderPoints = new LinkedList<>();

        public void addPoint(TrailPoint point) {
            this.actualPoints.addFirst(point);
            if (this.actualPoints.size() > TrailHolder.this.length) this.actualPoints.removeLast();
            this.renderPoints.clear();
            this.renderPoints.addAll(this.actualPoints);
        }

        public void update() {
            List<TrailPoint> modified = new ArrayList<>();
            float totalLength = 0;
            for (int i = 0; i < this.renderPoints.size() - 1; i++) {
                TrailPoint from = this.renderPoints.get(i);
                TrailPoint to = this.renderPoints.get(i + 1);
                float distance = (float) from.center().distanceTo(to.center());
                totalLength += distance;
                if (totalLength > TrailHolder.this.length) {
                    this.renderPoints.set(i + 1, this.interpolateTrailPoint((totalLength - TrailHolder.this.length) / distance, to, from));
                    modified.addAll(this.renderPoints.subList(0, i + 2));
                    totalLength = TrailHolder.this.length;
                    break;
                }
            }
            if (!modified.isEmpty()) {
                this.renderPoints.clear();
                this.renderPoints.addAll(modified);
            }
            float currentLength = 0;
            for (int i = 0; i < this.renderPoints.size() - 1; i++) {
                TrailPoint from = this.renderPoints.get(i);
                TrailPoint to = this.renderPoints.get(i + 1);
                float distance = (float) from.center().distanceTo(to.center());
                this.renderPoints.set(i, this.renderPoints.get(i).withWidth((totalLength - currentLength) * (TrailHolder.this.width / totalLength)));
                currentLength += distance;
            }
            if (this.renderPoints.size() > 1)
                this.renderPoints.set(this.renderPoints.size() - 1, this.renderPoints.getLast().withWidth(0.01f));
        }

        private TrailPoint interpolateTrailPoint(float progress, TrailPoint first, TrailPoint second) {
            return new TrailPoint(MDMath.lerpVec(progress, first.upper(), second.upper()), MDMath.lerpVec(progress, first.lower(), second.lower()));
        }
    }

    public record TrailPoint(Vec3 upper, Vec3 lower) {
        public Vec3 center() {
            return this.lower.add(this.upper).multiply(0.5, 0.5, 0.5);
        }

        public TrailPoint withWidth(float width) {
            Vec3 center = this.center();
            Vec3 upperVec = this.upper().subtract(center);
            Vec3 lowerVec = this.lower().subtract(center);
            return new TrailPoint(center.add(upperVec.normalize().scale(width / 2)), center.add(lowerVec.normalize().scale(width / 2)));
        }
    }
}
