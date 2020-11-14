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

Сервис-классы:
User, UserRepository, UserController, UserView и т.д. 

## Результат 
Результатом выполнения задания должен быть отдельный репозиторий с README.md файлом, 
который содержит описание задачи, проекта и инструкции запуска приложения через командную строку.


## Инструкция по запуску