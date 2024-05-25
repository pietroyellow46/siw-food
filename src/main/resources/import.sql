insert into chef (id, name, surname, date_birth, path_image) values(nextval('chef_seq'), 'Antonino', 'Cannavacciuolo', '1975-11-09', 'chef1.jpg');
insert into chef (id, name, surname, date_birth, path_image) values(nextval('chef_seq'), 'Carlo', 'Cracco', '1965-10-08', 'chef51.jpg');
insert into chef (id, name, surname, date_birth, path_image) values(nextval('chef_seq'), 'Iginio', 'Massari', '1946-05-04', 'chef101.jpg');
insert into chef (id, name, surname, date_birth, path_image) values(nextval('chef_seq'), 'Alessandro', 'Borghese', '1976-09-19', 'chef151.png');
insert into chef (id, name, surname, date_birth, path_image) values(nextval('chef_seq'), 'Joe', 'Bastianich', '1968-09-17', 'chef201.jpg');
insert into chef (id, name, surname, date_birth, path_image) values(nextval('chef_seq'), 'Max', 'Mariola', '1975-12-13', 'chef251.jpeg');
insert into chef (id, name, surname, date_birth, path_image) values(nextval('chef_seq'), 'Bruno', 'Barbieri', '1962-08-28', 'chef301.jpg');
insert into chef (id, name, surname, date_birth, path_image) values(nextval('chef_seq'), 'Giorgio', 'Locatelli', '1963-10-20', 'chef351.jpg');

insert into ingredient (id, name, path_image) values(nextval('ingredient_seq'), 'Pomodori', 'ingredient1.jpg');
insert into ingredient (id, name, path_image) values(nextval('ingredient_seq'), 'Olio oliva', 'ingredient51.jpg');
insert into ingredient (id, name, path_image) values(nextval('ingredient_seq'), 'Aglio', 'ingredient101.jpg');
insert into ingredient (id, name, path_image) values(nextval('ingredient_seq'), 'Basilico', 'ingredient151.jpg');
insert into ingredient (id, name, path_image) values(nextval('ingredient_seq'), 'Pasta', 'ingredient201.jpg');
insert into ingredient (id, name, path_image) values(nextval('ingredient_seq'), 'Formaggio Parmigiano Reggiano', 'ingredient251.jpg');
insert into ingredient (id, name, path_image) values(nextval('ingredient_seq'), 'Mozzarella', 'ingredient301.jpg');
insert into ingredient (id, name, path_image) values(nextval('ingredient_seq'), 'Prosciutto Crudo', 'ingredient351.jpg');
insert into ingredient (id, name, path_image) values(nextval('ingredient_seq'), 'Cipolla', 'ingredient401.jpg');
insert into ingredient (id, name, path_image) values(nextval('ingredient_seq'), 'Riso Arborio', 'ingredient451.jpg');

insert into recipe (id, nome, chef_id, main_image, extra_image1, extra_image2, extra_image3) values(nextval('recipe_seq'), 'Pizza Margherita', 1,'/images/recipe/margherita.png', null, null, null);
insert into recipe (id, nome, chef_id, main_image, extra_image1, extra_image2, extra_image3) values(nextval('recipe_seq'), 'Spaghetti alla Carbonara',251, '/images/logo-siw.png', null, null, null);
insert into recipe (id, nome, chef_id, main_image, extra_image1, extra_image2, extra_image3) values(nextval('recipe_seq'), 'Risotto alla Milanese',51, '/images/logo-siw.png', null, null, null);
insert into recipe (id, nome, main_image, extra_image1, extra_image2, extra_image3) values(nextval('recipe_seq'), 'Ravioli di Ricotta e Spinaci', '/images/logo-siw.png', null, null, null);
insert into recipe (id, nome, chef_id, main_image, extra_image1, extra_image2, extra_image3) values(nextval('recipe_seq'), 'Lasagne alla Bolognese', 301,'/images/logo-siw.png', null, null, null);
insert into recipe (id, nome, chef_id, main_image, extra_image1, extra_image2, extra_image3) values(nextval('recipe_seq'), 'Ossobuco alla Milanese', 351, '/images/logo-siw.png', null, null, null);
insert into recipe (id, nome, chef_id, main_image, extra_image1, extra_image2, extra_image3) values(nextval('recipe_seq'), 'Tiramisù', 101, '/images/logo-siw.png', null, null, null);
insert into recipe (id, nome, main_image, extra_image1, extra_image2, extra_image3) values(nextval('recipe_seq'), 'Pesto alla Genovese', '/images/logo-siw.png', null, null, null);
insert into recipe (id, nome, chef_id, main_image, extra_image1, extra_image2, extra_image3) values(nextval('recipe_seq'), 'Caprese Salad',151, '/images/logo-siw.png', null, null, null);
insert into recipe (id, nome, main_image, extra_image1, extra_image2, extra_image3) values(nextval('recipe_seq'), 'Risotto ai Frutti di Mare', '/images/logo-siw.png', null, null, null);


insert into used_ingredient (id, ingredient_id, recipe_id, quantity, measurement) values(nextval('used_ingredient_seq'), 1, 1, 200, 'grammi');
insert into used_ingredient (id, ingredient_id, recipe_id, quantity, measurement) values(nextval('used_ingredient_seq'), 1, 151, 300, 'grammi');
insert into used_ingredient (id, ingredient_id, recipe_id, quantity, measurement) values(nextval('used_ingredient_seq'), 51, 1, 10, 'millilitri');
insert into used_ingredient (id, ingredient_id, recipe_id, quantity, measurement) values(nextval('used_ingredient_seq'), 51, 151, 15, 'millilitri');
insert into used_ingredient (id, ingredient_id, recipe_id, quantity, measurement) values(nextval('used_ingredient_seq'), 51, 51, 5, 'millilitri');
insert into used_ingredient (id, ingredient_id, recipe_id, quantity, measurement) values(nextval('used_ingredient_seq'), 101, 1, 2, 'spicchi');
insert into used_ingredient (id, ingredient_id, recipe_id, quantity, measurement) values(nextval('used_ingredient_seq'), 151, 1, 0, 'quantobasta');
insert into used_ingredient (id, ingredient_id, recipe_id, quantity, measurement) values(nextval('used_ingredient_seq'), 201, 51, 200, 'grammi');
insert into used_ingredient (id, ingredient_id, recipe_id, quantity, measurement) values(nextval('used_ingredient_seq'), 201, 151, 150, 'grammi');
insert into used_ingredient (id, ingredient_id, recipe_id, quantity, measurement) values(nextval('used_ingredient_seq'), 251, 151, 10, 'grammi');
insert into used_ingredient (id, ingredient_id, recipe_id, quantity, measurement) values(nextval('used_ingredient_seq'), 301, 1, 0, 'quantobasta');
insert into used_ingredient (id, ingredient_id, recipe_id, quantity, measurement) values(nextval('used_ingredient_seq'), 301, 151, 50, 'grammi');
insert into used_ingredient (id, ingredient_id, recipe_id, quantity, measurement) values(nextval('used_ingredient_seq'), 451, 351, 200, 'grammi');



