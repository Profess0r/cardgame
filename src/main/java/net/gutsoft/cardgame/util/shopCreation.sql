insert 
into
	Card
	(name, rank, maxHealth, targetClass, multiusable, power, defence, description, price) 
values
	("warrior", 1, 10, "11000", true, 8, 2, "mighty warrior", 80),
	("archer", 1, 7, "11000", true, 5, 1, "fast archer", 80),
	("mage", 1, 5, "11000", true, 12, 0, "intelligent mage", 80),
	("heal", 1, 1, "01110", false, -5, 0, "restore health of player or card", 30),
	("shot", 1, 1, "10000", false, 5, 0, "cause direct damage to player or card", 30),
	("wall", 1, 15, "01000", false, 0, 2, "defence structure", 40),
	("tower", 1, 10, "01000", false, 4, 2, "defence structure, deal damage to attacking card", 50);

insert 
into
	Account
	(login, password, email, level, experience, health, money) 
values
	("shop", "shop", "shop", 1, 0, 0, 0);

insert 
into
	AccountCard
	(account_id, card_id) 
values
	((select id from Account where login = 'shop'), (select id from Card where name = 'warrior')),
	((select id from Account where login = 'shop'), (select id from Card where name = 'archer')),
	((select id from Account where login = 'shop'), (select id from Card where name = 'mage')),
	((select id from Account where login = 'shop'), (select id from Card where name = 'heal')),
	((select id from Account where login = 'shop'), (select id from Card where name = 'shot')),
	((select id from Account where login = 'shop'), (select id from Card where name = 'wall')),
	((select id from Account where login = 'shop'), (select id from Card where name = 'tower'));
	
