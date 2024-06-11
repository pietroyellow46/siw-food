insert into chef (id, name, surname, date_birth, path_image) values(nextval('chef_seq'), 'Antonino', 'Cannavacciuolo', '1975-11-09', 'chef1.jpg');
insert into chef (id, name, surname, date_birth, path_image) values(nextval('chef_seq'), 'Carlo', 'Cracco', '1965-10-08', 'chef51.jpg');
insert into chef (id, name, surname, date_birth, path_image) values(nextval('chef_seq'), 'Iginio', 'Massari', '1946-05-04', 'chef101.jpg');
insert into chef (id, name, surname, date_birth, path_image) values(nextval('chef_seq'), 'Alessandro', 'Borghese', '1976-09-19', 'chef151.png');
insert into chef (id, name, surname, date_birth, path_image) values(nextval('chef_seq'), 'Joe', 'Bastianich', '1968-09-17', 'chef201.jpg');
insert into chef (id, name, surname, date_birth, path_image) values(nextval('chef_seq'), 'Max', 'Mariola', '1975-12-13', 'chef251.jpeg');
insert into chef (id, name, surname, date_birth, path_image) values(nextval('chef_seq'), 'Bruno', 'Barbieri', '1962-08-28', 'chef301.jpg');
insert into chef (id, name, surname, date_birth, path_image) values(nextval('chef_seq'), 'Giorgio', 'Locatelli', '1963-10-20', 'chef351.jpg');


insert into ingredient (id, name, path_image, description) values(nextval('ingredient_seq'), 'Pomodori', 'ingredient1.jpg', 'Deliziosi pomodori freschi, ideali per preparare salse saporite, insalate estive e molti altri piatti mediterranei. Perfetti per dare colore e gusto.');

insert into ingredient (id, name, path_image, description) values(nextval('ingredient_seq'), 'Olio oliva', 'ingredient51.jpg', 'Olio extravergine di oliva, di qualità superiore, estratto a freddo per preservare tutte le proprietà nutritive e organolettiche. Ottimo per condimenti e cottura.');

insert into ingredient (id, name, path_image, description) values(nextval('ingredient_seq'), 'Aglio', 'ingredient101.jpg', 'Spicchi di aglio aromatico, essenziali per insaporire una vasta gamma di piatti, dalle salse alle carni, fino alle verdure. Un ingrediente fondamentale nella cucina mediterranea.');

insert into ingredient (id, name, path_image, description) values(nextval('ingredient_seq'), 'Basilico', 'ingredient151.jpg', 'Foglie di basilico fresco, dal profumo intenso e aromatico. Ideali per preparare il pesto alla genovese, guarnire pizze, insalate e molte altre pietanze.');

insert into ingredient (id, name, path_image, description) values(nextval('ingredient_seq'), 'Pasta', 'ingredient201.jpg', 'Pasta italiana di alta qualità, trafilata al bronzo e a lenta essiccazione. Disponibile in varie forme, perfetta per esaltare ogni tipo di sugo, dalle ricette tradizionali a quelle più innovative.');

insert into ingredient (id, name, path_image, description) values(nextval('ingredient_seq'), 'Formaggio Parmigiano Reggiano', 'ingredient251.jpg', 'Formaggio stagionato, dal sapore ricco e complesso. Ideale da grattugiare su paste, risotti, zuppe, o da gustare in scaglie con un buon bicchiere di vino.');

insert into ingredient (id, name, path_image, description) values(nextval('ingredient_seq'), 'Mozzarella', 'ingredient301.jpg', 'Formaggio fresco e filante, prodotto con latte vaccino di alta qualità. Ottimo per preparare la pizza, insalate capresi, e molti altri piatti. La sua consistenza morbida e il suo sapore delicato la rendono versatile.');

insert into ingredient (id, name, path_image, description) values(nextval('ingredient_seq'), 'Prosciutto Crudo', 'ingredient351.jpg', 'Prosciutto crudo stagionato, dal sapore dolce e leggermente salato. Perfetto per antipasti, panini gourmet e come accompagnamento per formaggi e frutta. Un’eccellenza della tradizione italiana.');

insert into ingredient (id, name, path_image, description) values(nextval('ingredient_seq'), 'Cipolla', 'ingredient401.jpg', 'Cipolle aromatiche, base perfetta per una vasta gamma di piatti, dalle zuppe alle salse, fino ai soffritti. Essenziali in cucina per aggiungere sapore e dolcezza naturale alle ricette.');

insert into ingredient (id, name, path_image, description) values(nextval('ingredient_seq'), 'Riso Arborio', 'ingredient451.jpg', 'Riso Arborio di alta qualità, con chicchi grandi e perlati. Ideale per preparare risotti cremosi e consistenti, ma anche per timballi e altre specialità della cucina italiana.');



insert into recipe (id, nome, description, chef_id, main_image, extra_image1, extra_image2, extra_image3) values(nextval('recipe_seq'), 'Pizza Margherita', 'Una pizza classica italiana con salsa di pomodoro, mozzarella fresca, e foglie di basilico, cotta nel forno a legna. Creata in onore della Regina Margherita, rappresenta i colori della bandiera italiana.', 1, '1Main.jpg', null, null, null);
insert into recipe (id, nome, description, chef_id, main_image, extra_image1, extra_image2, extra_image3) values(nextval('recipe_seq'), 'Spaghetti alla Carbonara', 'Un piatto di pasta cremoso preparato con uova, pecorino romano, guanciale croccante e pepe nero macinato fresco. Originaria del Lazio, è una delle ricette più amate e discusse, con numerose varianti e segreti tramandati.', 251, '51Main.jpg', null, null, null);
insert into recipe (id, nome, description, chef_id, main_image, extra_image1, extra_image2, extra_image3) values(nextval('recipe_seq'), 'Risotto alla Milanese', 'Un risotto cremoso tipico di Milano, aromatizzato con zafferano e parmigiano reggiano, servito al dente. Simbolo della cucina milanese, spesso accompagnato da ossobuco, rappresenta la tradizione culinaria lombarda.', 51, '101Main.jpg', null, null, null);
insert into recipe (id, nome, description, main_image, extra_image1, extra_image2, extra_image3) values(nextval('recipe_seq'), 'Ravioli di Ricotta e Spinaci', 'Deliziosi ravioli ripieni di ricotta fresca e spinaci, serviti con salsa di burro e salvia o pomodoro. Piatti tipici delle festività italiane, i ravioli variano di ripieno e salsa a seconda delle regioni.', '151Main.jpg', null, null, null);
insert into recipe (id, nome, description, chef_id, main_image, extra_image1, extra_image2, extra_image3) values(nextval('recipe_seq'), 'Lasagne alla Bolognese', 'Strati di pasta all uovo, ragù alla bolognese e besciamella, cotti al forno fino a ottenere una crosticina dorata. Un piatto iconico della cucina emiliana, noto per la sua ricchezza e complessità di sapori.', 301, '201Main.jpg', null, null, null);
insert into recipe (id, nome, description, chef_id, main_image, extra_image1, extra_image2, extra_image3) values(nextval('recipe_seq'), 'Ossobuco alla Milanese', 'Stinco di vitello tenero brasato con verdure, vino bianco e brodo, servito con gremolata e risotto alla milanese. Questo piatto tradizionale è un omaggio alla cucina milanese, noto per la sua delicatezza e sapore intenso.', 351, '251Main.jpg', null, null, null);
insert into recipe (id, nome, description, chef_id, main_image, extra_image1, extra_image2, extra_image3) values(nextval('recipe_seq'), 'Tiramisù', 'Un dessert italiano classico a base di strati di savoiardi imbevuti di caffè e crema di mascarpone, spolverato con cacao amaro. Originario del Veneto, il tiramisù è diventato un simbolo della pasticceria italiana in tutto il mondo.', 101, '301Main.jpg', null, null, null);
insert into recipe (id, nome, description, main_image, extra_image1, extra_image2, extra_image3) values(nextval('recipe_seq'), 'Pesto alla Genovese', 'Una salsa saporita fatta con basilico fresco, pinoli, parmigiano, aglio e olio extravergine di oliva, ideale per condire la pasta. Emblema della cucina ligure, il pesto è apprezzato per la sua freschezza e semplicità.', '351Main.jpg', null, null, null);
insert into recipe (id, nome, description, chef_id, main_image, extra_image1, extra_image2, extra_image3) values(nextval('recipe_seq'), 'Caprese Salad', 'Una semplice e rinfrescante insalata italiana preparata con pomodori freschi, mozzarella di bufala, basilico e olio di oliva. Nata a Capri, questa insalata è un simbolo dell estate italiana e della cucina mediterranea.', 151, '401Main.jpg', null, null, null);
insert into recipe (id, nome, description, main_image, extra_image1, extra_image2, extra_image3) values(nextval('recipe_seq'), 'Risotto ai Frutti di Mare', 'Un delizioso risotto ai frutti di mare, preparato con una varietà di pesce fresco e crostacei, cotto alla perfezione. Tipico delle regioni costiere italiane, questo risotto è un piatto estivo perfetto per gli amanti del mare.', '451Main.jpg', null, null, null);

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



