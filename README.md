# java-explore-with-me
Ссылка на пул-реквест: [feature_rating_events -> main_svc](https://github.com/tonychem/explore-with-me/pull/3)

Для определения рейтинга события использовался статистический [коэффициент](https://www.evanmiller.org/how-not-to-sort-by-average-rating.html). 
Его значение недоступно пользователю, все расчеты происходят на уровне БД. Для определения рейтинга подборок событий 
использовалось среднее значение такого коэффициента из составляющих подборку событий. Оценку добавлять могут только зарегистрированные пользователи опубликованным событиям.
