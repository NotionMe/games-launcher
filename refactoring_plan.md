# 📋 План рефакторингу Game Launcher

Цей документ описує поетапний план покращення архітектури проекту. Мета — зменшити зв’язність між класами, прибрати дублювання коду та винести бізнес-логіку з контролерів.

---

## 🏗 Етап 1: "Швидкі перемоги" (Cleanup & Essentials)
*Ціль: Прибрати очевидне дублювання та виправити іменування без зміни складної логіки.*

### 1.1 Виправлення іменування
- [ ] Перейменувати `LauchingServise` -> `GameLauncherService` (або просто `GameLauncher`).
- [ ] Виправити пакет/імпорти у всіх класах, де він використовується.

### 1.2 WindowDragHandler (Видалення дублів)
- [ ] Створити клас `ua.notion.ui.fx.WindowDragHandler`.
- [ ] Реалізувати статчиний метод `attach(Node node, Stage stage)`.
- [ ] Видалити методи `handlePressAction`/`handleMovementAction` з `MainMenuController`.
- [ ] Видалити методи `headerPressAction`/`headerMoveAction` з `SideDrawerController`.
- [ ] Замінити їх викликом `WindowDragHandler.attach(...)` у `initialize()`.

### 1.3 FxAnimations (Анімації окремо)
- [ ] Створити клас `ua.notion.ui.fx.FxAnimations`.
- [ ] Винести логіку `FadeTransition` (зміна фону) з `MainMenuController` (`transitionToBackground`) у цей сервіс.
- [ ] Винести логіку `TranslateTransition` (сайдбар) з `SideDrawerController` у цей сервіс.

---

## 🧭 Етап 2: Навігація та розв'язка контролерів
*Ціль: Прибрати пряму залежність через статичні поля та ручне управління `getChildren().remove()`.*

### 2.1 NavigationService
- [ ] Створити клас `ua.notion.ui.navigation.NavigationService`.
- [ ] Додати методи:
    - `setRoot(StackPane root)` — для ініціалізації.
    - `navigateTo(String fxmlPath)` — для додавання екрану в стек.
    - `goBack()` — для безпечного видалення верхнього екрану.
    - `openModal(String fxmlPath)` — для Settings.

### 2.2 Рефакторинг MainMenuController
- [ ] Видалити логіку ручного створення `FXMLLoader` у `initialNode()`.
- [ ] Замінити пряме додавання `gameSelectView` на виклик `NavigationService.navigateTo(...)`.
- [ ] Ініціалізувати `NavigationService` у `initialize()`.

### 2.3 Рефакторинг GameSelectController
- [ ] Прибрати статичні поля `MainMenuController`.
- [ ] У методі `onBackButton` замінити складну логіку (`children.remove(size-1)`) на `NavigationService.goBack()`.
- [ ] Ін'єктити `NavigationService` замість передачі всього `MainMenuController`.

---

## 🚀 Етап 3: Логіка запуску та Інфраструктура
*Ціль: Контролери не повинні знати про ProcessBuilder та аргументи командного рядка.*

### 3.1 Відокремлення GameLauncher
- [ ] У `GameLauncherService` додати метод `installGame(GameConfig config)` або `runInstaller(...)`.
- [ ] Перенести логіку формування списку аргументів (`List.of(script, proton, ...)`) з `GameSelectController.onRunInstaller` у цей сервіс.
- [ ] Контролер має лише збирати дані з полів і передавати їх у сервіс.

### 3.2 Ресурси та константи
- [ ] Перевірити `Constants.java`. Всі шляхи до файлів (скрипти, іконки) мають бути там або в `ResourceService`.
- [ ] Прибрати хардкод шляхів у контролерах.

---

## 💾 Етап 4: Дані та Стан (Future)
*Ціль: Збереження стану програми.*

### 4.1 GameRepository
- [ ] Якщо планується редагування ігор, створити `GameRepository` для збереження JSON/DB.
- [ ] `GameService` має використовувати репозиторій, а не читати файли напряму (де це можливо).

---

## ✅ Definition of Done (Критерії завершення)

1. **Компіляція:** Проект збирається без помилок на кожному етапі.
2. **Чистота:** У контролерах немає `ProcessBuilder`, `Themes/Styles` логіки, дубльованих `MouseEvent` хендлерів.
3. **Навігація:** Кнопка "Назад" працює через сервіс, а не через видалення індексу в списку.
4. **Стабільність:** Перетягування вікна та сайдбару працює плавно.
