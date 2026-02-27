# ARCHITECTURE — ShipConstructor

## Назначение
`ShipConstrucor` — локальный инженерный инструмент для проектирования корабельных модулей и предварительного расчета шасси. Приложение работает как JavaFX desktop UI с сохранением модулей в MySQL и поддержкой локальных JSON-черновиков.

## Технологический стек
- Java 17
- Maven
- JavaFX 21
- Jackson
- MySQL Connector/J

## Высокоуровневый поток
1. Пользователь редактирует карточку модуля в UI.
2. Валидация полей и JSON-блоков перед сохранением.
3. Формируется доменная модель модуля.
4. Сохранение в `ShipModules` (insert/update, автоподбор `ModuleID`).
5. Для диагностики может запускаться граф шасси и pre-solver нагрузки.
6. По необходимости данные сохраняются/загружаются как локальный JSON draft.

## Слои
### 1) UI layer
- Пакет: `org.example.shipconstructor.ui`
- Точка входа: `ModuleDesignerLauncher`
- Роль: форма редактирования, действия New/Duplicate/Clear, валидация и вызов сервисов.

### 2) Application/service layer
- Пакет: `org.example.shipconstructor.service`
- Роль: orchestration между UI, domain, DB и локальным IO.

### 3) Domain model
- Пакет: `org.example.shipconstructor.domain`
- Роль: структура модуля и бизнес-сущности конструктора.

### 4) Chassis subsystem
- Пакет: `org.example.shipconstructor.chassis`
- Подпакеты:
  - `domain` — модель расчета шасси;
  - `graph` — построение структурного графа;
  - `service` — вычислительные и диагностические сервисы;
  - `ui` — представление/диагностика для пользователя;
  - `demo` — демонстрационные сценарии.
- Роль: предрасчет нагрузки, осевые ограничения и диагностические метрики.

### 5) Persistence layer
- Пакет: `org.example.shipconstructor.db`
- Роль:
  - загрузка env-конфигурации (`.env`/`project.env`);
  - JDBC-доступ к MySQL;
  - операции чтения/записи таблицы `ShipModules`.

## Конфигурация
- Приоритет источников конфигурации БД: `.env` (предпочтительно), затем `project.env`.
- Поддерживаются настройки через `DB_HOST/PORT/NAME` или полный `DB_URL`.

## Контракты и интеграции
- Основная таблица: `ShipModules` (MySQL `EXOLOG`).
- Документация инженерных коэффициентов и модели:
  - `docs/ENGINEERING_REFERENCE.md`
  - `docs/CHASSIS_CALCULATION_MODEL.md`
- Сэмплы коэффициентов: `data/reference/chassis_coefficients.sample.json`.

## Точки расширения
- Добавление новых атрибутов модуля: расширение `domain` + формы `ui` + mapping в `db`.
- Новые стадии расчета шасси: расширение `chassis/service` и модели в `chassis/domain`.
- Дополнительные форматы обмена: расширение JSON IO в сервисном слое.

## Риски
- Связность UI и прикладной логики при росте числа полей.
- Риск рассинхронизации между JSON-предпросмотром и DB-схемой.
- Изменения в инженерных коэффициентах требуют повторной калибровки расчетов.

## Быстрая навигация
- Entry point: `src/main/java/org/example/shipconstructor/ui/ModuleDesignerLauncher.java`
- UI: `src/main/java/org/example/shipconstructor/ui/`
- Chassis: `src/main/java/org/example/shipconstructor/chassis/`
- DB: `src/main/java/org/example/shipconstructor/db/`
- Domain: `src/main/java/org/example/shipconstructor/domain/`
