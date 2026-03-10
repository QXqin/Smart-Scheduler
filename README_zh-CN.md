# 智能时间表系统 (Smart Scheduler)

> 本地原生构建的安卓智能 AI 学习计划与待办事项管理助手。

[English](./README.md) | [中文](./README_zh-CN.md)

![Android](https://img.shields.io/badge/Android-Kotlin-green?logo=android)
![Jetpack Compose](https://img.shields.io/badge/Jetpack_Compose-Modern_UI-blue)
![Room Database](https://img.shields.io/badge/Room-Database-blue?logo=sqlite)
![Hilt](https://img.shields.io/badge/Dagger_Hilt-DI-orange)

专为学生与独立开发者打造的 Android 应用，旨在帮助用户高效规划学习日程、导入固定大学课表，并利用 AI 生成式引擎动态分解与分配每日待办大纲，拒绝死板的日程表。

## 核心特性 (Features)

- **AI 动态日程生成**：未填时长的任务可交由 AI 自动估算耗时，并在固定课程或事件的空隙中智能穿插安排。
- **固定课程与日历体系**：支持管理校内固定重复日程，基于纯本地引擎。
- **智能待办清单**：支持一键敏捷录入，并为高频任务引入“每日重复 (Daily)”特性；打勾完成后自动将截止日期顺延1天。
- **Room 纯本地化存储**：底层全程依赖 SQLite 进行持久化存储状态，纯断网可用无隐私泄露担忧。
- **现代化架构**：应用全局引入 Kotlin Coroutines (协程) + StateFlow 状态机，结合 Dagger Hilt 依赖注入与 Jetpack Compose 响应式 UI 渲染，体验纵享丝滑。

## 快速上手 (Quick Start)

### 环境要求

- Android Studio Koala Feature Drop 及以上版本
- Java Development Kit (JDK) 17 
- Android 真机测试设备或高版本安卓模拟器

### 安装步骤

```bash
# 克隆仓库
git clone https://github.com/USERNAME/smart-scheduler-android.git
cd smart-scheduler-android
```

在 **Android Studio** 中直接打开该项目根目录，等待右下角的 Gradle 构建进程与依赖自动拉取完毕。

### 使用与运行

1. 点击 Android Studio 顶部的绿色 `▶ Run` (运行) 按钮。
2. 确保您的 Android 手机或平板已开启开发者模式并允许 USB/无线调试。
3. 打开“智能时间表”，切换至待办页即可畅享排程！

## 项目结构 (Structure)

```
smart-scheduler-android/
├── app/
│   ├── src/main/java/com/smartscheduler/app/
│   │   ├── data/       # Room 实体类、数据访问对象 (DAO) 以及 Repository 层
│   │   ├── di/         # Dagger Hilt 依赖注入配置模块
│   │   ├── domain/     # 数据业务实体
│   │   └── ui/         # MVVM 视图层、Compose 组件与 ViewModels
│   └── build.gradle.kts
└── build.gradle.kts
```

## 贡献 (Contributing)

欢迎提交 Pull Requests 进行功能共建！若是针对核心功能重构或大型代码变更，烦请先开启 Issue 提出您的优化想法哦。

## 开源协议 (License)

[MIT](./LICENSE) © 2026 
