package com.iafenvoy.minedash.trail;

import com.iafenvoy.minedash.util.MDMath;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TrailHolder {
    private final PointHolder vertical = new PointHolder(), horizontal = new PointHolder();
    private final float width, maxLength;

    public TrailHolder(float width, int maxLength) {
        this.width = width;
        this.maxLength = maxLength;
    }

    public List<List<TrailPoint>> getVerticalPoints() {
        return this.vertical.renderPoints;
    }

    public List<List<TrailPoint>> getHorizontalPoints() {
        return this.horizontal.renderPoints;
    }

    public void tick(Vec3 pos, Vec3 delta, boolean addPoint) {
        if (addPoint) {
            float yaw = MDMath.positionToYaw(delta);
            float pitch = MDMath.positionToPitch(delta);
            Vec3 upper = MDMath.rotationToPosition(pos, this.width / 2f, pitch - 90, yaw);
            Vec3 lower = MDMath.rotationToPosition(pos, this.width / 2f, pitch + 90, yaw);
            Vec3 offset = upper.subtract(lower).cross(delta).normalize().scale(this.width / 2);
            Vec3 upper1 = pos.add(offset);
            Vec3 lower1 = pos.add(offset.scale(-1));
            this.vertical.tick(new TrailPoint(upper, lower));
            this.horizontal.tick(new TrailPoint(upper1, lower1));
        } else {
            this.vertical.tick(null);
            this.horizontal.tick(null);
        }
    }

    private class PointHolder {
        //List allow null
        private final List<TrailPoint> actualPoints = new LinkedList<>();
        private final List<List<TrailPoint>> renderPoints = new LinkedList<>();

        public void tick(TrailPoint point) {
            this.actualPoints.addFirst(point);
            if (this.actualPoints.size() > TrailHolder.this.maxLength) this.actualPoints.removeLast();
            //Spilt segments
            List<List<TrailPoint>> segments = new ArrayList<>();
            List<TrailPoint> cache = new LinkedList<>();
            for (TrailPoint p : this.actualPoints) {
                if (p != null) cache.add(p);
                else {
                    if (!cache.isEmpty()) segments.add(cache);
                    cache = new LinkedList<>();
                }
            }
            if (!cache.isEmpty()) segments.add(cache);
            //Process segment
            this.renderPoints.clear();
            for (List<TrailPoint> segment : segments) {
                if (segment.size() < 2) continue;
                float totalLength = this.trimTrailToMaxLength(segment);
                if (totalLength <= 0) continue;
                this.calculateAndSetPointWidths(segment, totalLength);
                this.renderPoints.add(segment);
            }
        }

        private float trimTrailToMaxLength(List<TrailPoint> segment) {
            float totalLength = 0;

            for (int i = 0; i < segment.size() - 1; i++) {
                TrailPoint from = segment.get(i), to = segment.get(i + 1);
                float distance = (float) from.center().distanceTo(to.center());
                totalLength += distance;

                if (totalLength > TrailHolder.this.maxLength) {
                    float ratio = (totalLength - TrailHolder.this.maxLength) / distance;
                    TrailPoint interpolated = this.interpolateTrailPoint(ratio, to, from);
                    List<TrailPoint> trimmed = new ArrayList<>(segment.subList(0, i + 1));
                    trimmed.add(interpolated);
                    segment.clear();
                    segment.addAll(trimmed);

                    totalLength = TrailHolder.this.maxLength;
                    break;
                }
            }

            return totalLength;
        }

        //Set Width
        private void calculateAndSetPointWidths(List<TrailPoint> segment, float totalLength) {
            float currentLength = 0;
            float widthRatio = TrailHolder.this.width / totalLength;
            for (int i = 0; i < segment.size() - 1; i++) {
                TrailPoint from = segment.get(i), to = segment.get(i + 1);
                float distance = (float) from.center().distanceTo(to.center());
                //Set current width
                float width = (totalLength - currentLength) * widthRatio;
                segment.set(i, from.withWidth(width));

                currentLength += distance;
            }
            //Set last to min
            if (!segment.isEmpty()) {
                TrailPoint last = segment.getLast();
                segment.set(segment.size() - 1, last.withWidth(0.01f));
            }
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
