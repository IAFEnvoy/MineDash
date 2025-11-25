package com.iafenvoy.minedash.data;

import com.iafenvoy.minedash.util.MDMath;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TrailData {
    private final PointHolder x = new PointHolder(), y = new PointHolder(), z = new PointHolder();
    private final float width, maxLength;

    public TrailData(float width, int maxLength) {
        this.width = width;
        this.maxLength = maxLength;
    }

    public List<List<TrailPoint>> getXTrails() {
        return this.x.renderPoints;
    }

    public List<List<TrailPoint>> getYTrails() {
        return this.y.renderPoints;
    }

    public List<List<TrailPoint>> getZTrails() {
        return this.z.renderPoints;
    }

    public void tick(Vec3 pos, boolean addPoint) {
        if (addPoint) {
            float unit = this.width / 2 / Mth.SQRT_OF_TWO;
            this.x.tick(new TrailPoint(pos.subtract(unit, 0, 0), pos.add(unit, 0, 0)));
            this.y.tick(new TrailPoint(pos.subtract(0, unit, 0), pos.add(0, unit, 0)));
            this.z.tick(new TrailPoint(pos.subtract(0, 0, unit), pos.add(0, 0, unit)));
        } else {
            this.x.tick(null);
            this.y.tick(null);
            this.z.tick(null);
        }
    }

    private class PointHolder {
        //List allow null
        private final List<TrailPoint> actualPoints = new LinkedList<>();
        private final List<List<TrailPoint>> renderPoints = new LinkedList<>();

        public void tick(@Nullable TrailPoint point) {
            this.actualPoints.addFirst(point);
            if (this.actualPoints.size() > TrailData.this.maxLength) this.actualPoints.removeLast();
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

                if (totalLength > TrailData.this.maxLength) {
                    float ratio = (totalLength - TrailData.this.maxLength) / distance;
                    TrailPoint interpolated = this.interpolateTrailPoint(ratio, to, from);
                    List<TrailPoint> trimmed = new ArrayList<>(segment.subList(0, i + 1));
                    trimmed.add(interpolated);
                    segment.clear();
                    segment.addAll(trimmed);

                    totalLength = TrailData.this.maxLength;
                    break;
                }
            }

            return totalLength;
        }

        //Set Width
        private void calculateAndSetPointWidths(List<TrailPoint> segment, float totalLength) {
            float currentLength = 0;
            float widthRatio = TrailData.this.width / totalLength;
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
