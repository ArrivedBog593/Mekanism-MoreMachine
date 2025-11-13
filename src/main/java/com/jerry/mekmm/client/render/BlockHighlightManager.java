package com.jerry.mekmm.client.render;

import net.minecraft.core.BlockPos;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BlockHighlightManager {

    private static final BlockHighlightManager INSTANCE = new BlockHighlightManager();
    // 存储高亮方块及其数据
    private final Map<BlockPos, HighlightData> highlightedBlocks = new ConcurrentHashMap<>();

    private BlockHighlightManager() {
    }

    public static BlockHighlightManager getInstance() {
        return INSTANCE;
    }

    /**
     * 添加一个高亮方块
     *
     * @param pos      方块位置
     * @param duration 持续时间 (ticks)
     */
    public void addHighlight(BlockPos pos, int duration) {
        highlightedBlocks.put(pos, new HighlightData(duration));
    }

    /**
     * 添加一个高亮方块 (默认 10 秒 = 200 ticks)
     */
    public void addHighlight(BlockPos pos) {
        addHighlight(pos, 200);
    }

    /**
     * 添加一个带颜色的高亮方块
     */
    public void addHighlight(BlockPos pos, int duration, float r, float g, float b) {
        highlightedBlocks.put(pos, new HighlightData(duration, r, g, b));
    }

    /**
     * 移除高亮
     */
    public void removeHighlight(BlockPos pos) {
        highlightedBlocks.remove(pos);
    }

    /**
     * 检查方块是否被高亮
     */
    public boolean isHighlighted(BlockPos pos) {
        return highlightedBlocks.containsKey(pos);
    }

    /**
     * 获取高亮数据
     */
    public HighlightData getHighlightData(BlockPos pos) {
        return highlightedBlocks.get(pos);
    }

    /**
     * 每 tick 更新,减少剩余时间
     * 修复: 使用 Iterator 而不是 removeIf,避免在遍历时修改 entry
     */
    public void tick() {
        Iterator<Map.Entry<BlockPos, HighlightData>> iterator = highlightedBlocks.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<BlockPos, HighlightData> entry = iterator.next();
            HighlightData data = entry.getValue();
            // 减少剩余时间
            data.remainingTicks--;
            // 如果时间到了,移除
            if (data.remainingTicks <= 0) {
                iterator.remove();
            }
        }
    }

    /**
     * 清空所有高亮
     */
    public void clear() {
        highlightedBlocks.clear();
    }

    /**
     * 获取所有高亮的方块位置
     */
    public Iterable<BlockPos> getHighlightedPositions() {
        return highlightedBlocks.keySet();
    }

    /**
     * 高亮数据
     */
    public static class HighlightData {
        //不是final,需要每tick修改
        int remainingTicks;
        private final float red;
        private final float green;
        private final float blue;
        protected final float baseAlpha;

        //默认红色
        public HighlightData(int ticks) {
            this(ticks, 1.0f, 0.0f, 0.0f);
        }

        //默认alpha
        public HighlightData(int ticks, float r, float g, float b) {
            this(ticks, r, g, b, 1.0f);
        }

        public HighlightData(int ticks, float r, float g, float b, float alpha) {
            this.remainingTicks = ticks;
            this.baseAlpha = alpha;
            this.red = r;
            this.green = g;
            this.blue = b;
        }

        public int getRemainingTicks() {
            return remainingTicks;
        }

        public float getRed() {
            return red;
        }

        public float getGreen() {
            return green;
        }

        public float getBlue() {
            return blue;
        }

        /**
         * 获取当前透明度 (带闪烁效果)
         */
        public float getAlpha(long gameTime) {
            //闪烁效果: 使用正弦波
            //0.4 - 1.0
            float flash = (float) (Math.sin(gameTime * 0.5) * 0.3 + 0.7);
            return baseAlpha * flash;
        }
    }
}
