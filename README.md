# Module_1_3 
# Реализовать консольное MVC приложение

## Постанока задачи
Пользователь в консоли должен иметь возможность создания, получения, редактирования и удаления данных. 

Архитектура приложения имеет следующие слои:
1.  model - POJO классы;
2.  repository - классы, реализующие доступ к текстовым файлам;
3.  controller - обработка запросов от пользователя;
4.  view - все данные, необходимые для работы с консолью.
    
Приложение имеет следующие сущности: 
1.  User (id, firstName, lastName, List posts, Region region) 
2.  Post (id, content, created, updated) 
3.  Region (id, name) 
4.  Role (enum ADMIN, MODERATOR, USER)

В качестве хранилища данных необходимо использовать текстовые файлы: users.txt, posts.txt, regions.txt

## Рекомендации
Для репозиторного слоя желательно использовать базовый интерфейс: interface GenericRepository<T,ID>
interface UserRepository extends GenericRepository<User, Long>
class JavaIOUserRepositoryImpl implements UserRepository

Интерфейсы сервисы:
User, UserRepository, UserController, UserView и т.д. 

## Результат 
Результатом выполнения задания должен быть отдельный репозиторий с README.md файлом, 
который содержит описание задачи, проекта и инструкции запуска приложения через командную строку.

# Module_1_7 
# Реализовать консольное MVC приложение

## Постанока задачи
В качестве хранилища данных необходимо использовать CSV файлы:
users.csv, posts.csv, regions.csv

## Рекомендации
Для работы с CSV необходимо использовать библиотеку opencsv (https://mvnrepository.com/artifact/com.opencsv/opencsv) и maven для импорта библиотеки.

## Результат 
Результатом выполнения задания должен быть отдельный слой приложения, реализованного в рамках модуля  Java IO/NIO под названием csv. 
Слой controller должен использовать csv реализацию.

## Инструкция по запуску