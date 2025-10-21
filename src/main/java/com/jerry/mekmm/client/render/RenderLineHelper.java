package com.jerry.mekmm.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class RenderLineHelper {

    private static final ResourceLocation WHITE_TEX = ResourceLocation.fromNamespaceAndPath("minecraft", "textures/misc/white.png");

    public static void renderLine(PoseStack poseStack, MultiBufferSource buffers, Vector3f start, Vector3f end, float r, float g, float b, float thickness) {
        // 计算向量和长度
        Vector3f dir = new Vector3f(end).sub(start);
        float len = dir.length();
        if (len <= 1e-5) return;

        // 归一化方向
        dir.div(len);

        // 在起点渲染
        poseStack.pushPose();
        poseStack.translate(start.x(), start.y(), start.z());

        Matrix4f matrix = poseStack.last().pose();
        VertexConsumer vc = buffers.getBuffer(RenderType.entityTranslucentEmissive(WHITE_TEX));

        // 计算垂直向量基（u, v），用于定义截面
        Vector3f u = new Vector3f();
        if (Math.abs(dir.y()) < 0.99f)
            u.set(0, 1, 0).cross(dir).normalize();
        else
            u.set(1, 0, 0).cross(dir).normalize();

        Vector3f v = new Vector3f(dir).cross(u).normalize();

        // 光束半径
        float rHalf = thickness / 2f;
        int light = 0xF000F0;
        int overlay = 0;

        // 用 4 个矩形近似圆柱（比 20 面快很多）
        int segments = 4;
        for (int i = 0; i < segments; i++) {
            double a0 = (Math.PI * 2 * i) / segments;
            double a1 = (Math.PI * 2 * (i + 1)) / segments;

            Vector3f p0 = new Vector3f(u).mul((float) Math.cos(a0) * rHalf)
                    .add(new Vector3f(v).mul((float) Math.sin(a0) * rHalf));
            Vector3f p1 = new Vector3f(u).mul((float) Math.cos(a1) * rHalf)
                    .add(new Vector3f(v).mul((float) Math.sin(a1) * rHalf));

            // 起点与终点
            Vector3f s0 = new Vector3f(p0);
            Vector3f s1 = new Vector3f(p1);
            Vector3f e0 = new Vector3f(p0).add(new Vector3f(dir).mul(len));
            Vector3f e1 = new Vector3f(p1).add(new Vector3f(dir).mul(len));

            addQuad(vc, matrix, s0, s1, e1, e0, r, g, b, 1.0f, light, overlay);
        }

        poseStack.popPose();
    }

    /**
     * 绘制一个四边形面片
     */
    private static void addQuad(VertexConsumer vc, Matrix4f matrix, Vector3f p1, Vector3f p2, Vector3f p3, Vector3f p4, float r, float g, float b, float a, int light, int overlay) {
        vc.addVertex(matrix, p1.x(), p1.y(), p1.z()).setColor(r, g, b, a).setUv(0, 0).setLight(light).setOverlay(overlay).setNormal(0, 0, 0);
        vc.addVertex(matrix, p2.x(), p2.y(), p2.z()).setColor(r, g, b, a).setUv(1, 0).setLight(light).setOverlay(overlay).setNormal(0, 0, 0);
        vc.addVertex(matrix, p3.x(), p3.y(), p3.z()).setColor(r, g, b, a).setUv(1, 1).setLight(light).setOverlay(overlay).setNormal(0, 0, 0);
        vc.addVertex(matrix, p4.x(), p4.y(), p4.z()).setColor(r, g, b, a).setUv(0, 1).setLight(light).setOverlay(overlay).setNormal(0, 0, 0);
    }
}
