# SuperESRMod

一个 NeoForge 模组，在 Minecraft 游戏内提供 **NVIDIA DLSS** 和 **AMD FSR** 超分辨率以及帧生成支持。

- 模组版本：1.0.0
- 适配 Minecraft：1.26.1 (NeoForge)
- 加载端：CLIENT

## 功能

| 功能 | 平台 | 说明 |
|------|------|------|
| **FSR 1.0** | 全平台（Windows / Linux / Android） | 纯 GLSL 实现，开箱即用，兼容所有 GPU |
| **DLSS** | Windows 仅 | 需 RTX 20 系及以上 GPU + `nvngx_dlss.dll` |
| **FSR 2/3** | 全平台 | 需 AMD FidelityFX SDK 原生库 |
| **帧生成 (FG)** | Windows / 高版本 GPU | DLSS 3 FG（RTX 40+）或 FSR 3 FG |

## 兼容性

- **Sodium / Embeddium**：通过 Pseudo Mixin 软兼容，不强制依赖
- **Iris 光影**：在 Iris 光影 pass 完成后执行超分，不破坏光影效果
- **Fabric / Forge**：本模组仅支持 NeoForge

## 渲染流程

```
游戏以内部缩放比（如 0.5x）渲染世界到低分辨率帧缓冲
        │
        ▼
┌─ 超分放大 pass ─────────────────────────┐
│  FSR1: EASU 上采样 + RCAS 锐化 (GLSL)  │
│  DLSS: 原生 SDK 时域超分               │
└─────────────────────────────────────────┘
        │
        ▼
可选：帧生成在已渲染帧之间插入中间帧
        │
        ▼
输出到屏幕
```

## 构建

```bash
./gradlew build
```

产物在 `build/libs/superesrmod-1.0.0.jar`。

## 安装

1. 安装 [NeoForge](https://neoforged.net/) for Minecraft 1.26.1
2. 把 `superesrmod-1.0.0.jar` 放入 `mods/` 文件夹
3. 可选：安装 Sodium (Embeddium) 和 Iris 获得更好性能
4. 启动游戏，在 选项 → 视频设置 → SuperESRMod 中配置

### DLSS 额外步骤

DLSS 需要 `nvngx_dlss.dll`：
- 通过 NVIDIA App 安装 "DLSS 插件"，或
- 手动把 `nvngx_dlss.dll` 放入 `mods/superesrmod/natives/`

### FSR 2/3 额外步骤

把 `ffx_fsr2_*.dll`（Windows）或 `libffx_fsr2_*.so`（Linux）放入 `mods/superesrmod/natives/`。

## 配置项

| 配置 | 默认值 | 说明 |
|------|--------|------|
| `upscaleType` | FSR1 | OFF / DLSS / FSR1 / FSR2 |
| `internalScale` | 0.67 | 内部渲染缩放比 (0.25~1.0) |
| `enableSharpness` | true | 启用锐化 |
| `sharpness` | 1.0 | 锐化强度 (0.0~2.0) |
| `frameGenType` | OFF | OFF / DLSS3_FG / FSR3_FG |
| `fsrQualityMode` | BALANCED | QUALITY(0.67) / BALANCED(0.59) / PERFORMANCE(0.50) / ULTRA(0.33) |
| `enableDebugOverlay` | false | 屏幕左上角显示调试信息 |

## 项目结构

```
superesrmod/
├── build.gradle / settings.gradle / gradle.properties
├── src/main/java/com/superesrmod/
│   ├── SuperESRMod.java              # 模组入口
│   ├── config/ModConfig.java         # NeoForge 配置
│   ├── upscale/
│   │   ├── UpscaleManager.java       # 渲染调度中心
│   │   ├── UpscaleProcessor.java     # 超分器接口
│   │   ├── UpscaleType.java
│   │   ├── RenderTargets.java        # 低分辨率帧缓冲
│   │   ├── dlss/                     # DLSS 原生集成
│   │   ├── fsr/                      # FSR1 GLSL 实现
│   │   └── framegen/                 # 帧生成调度
│   ├── mixin/                        # Vanilla/Sodium/Iris 兼容
│   ├── platform/                     # OS/GPU 检测
│   └── gui/                          # 配置界面
└── src/main/resources/
    ├── META-INF/neoforge.mods.toml
    ├── superesrmod.mixins.json
    └── assets/superesrmod/
        ├── lang/{en_us,zh_cn}.json
        └── shaders/core/             # FSR EASU/RCAS GLSL
```

## License

MIT
