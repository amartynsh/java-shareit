CREATE TABLE IF NOT EXISTS public.users (
	id int8 GENERATED BY DEFAULT AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1 NO CYCLE) NOT NULL,
	"name" varchar NULL,
	email varchar NULL,
	CONSTRAINT user_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.items (
	id int8 GENERATED BY DEFAULT AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1 NO CYCLE) NOT NULL,
	"name" varchar NOT NULL,
	description varchar NULL,
	available bool NULL,
	owner_id int8 NULL,
	CONSTRAINT item_pk PRIMARY KEY (id),
	CONSTRAINT item_user_fk FOREIGN KEY (owner_id) REFERENCES public.users(id)
);

CREATE TABLE IF NOT EXISTS public.bookings (
	id int8 GENERATED BY DEFAULT AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1 NO CYCLE) NOT NULL,
	status varchar NULL,
	item_id int8 NULL,
	booker_id int8 NULL,
	start_time timestamp NULL,
	end_time timestamp NULL,
	CONSTRAINT booking_pk PRIMARY KEY (id),
	CONSTRAINT booking_items_fk FOREIGN KEY (item_id) REFERENCES public.items(id),
	CONSTRAINT booking_users_fk FOREIGN KEY (booker_id) REFERENCES public.users(id)

);

CREATE TABLE  IF NOT EXISTS public.comments (
	"text" varchar NULL,
	item_id int8 NULL,
	author_id int8 NULL,
	create_date timestamp NULL,
	id int8 GENERATED BY DEFAULT AS IDENTITY NOT NULL,
	CONSTRAINT comments_pk PRIMARY KEY (id),
	CONSTRAINT comments_items_fk FOREIGN KEY (item_id) REFERENCES public.items(id),
	CONSTRAINT comments_users_fk FOREIGN KEY (author_id) REFERENCES public.users(id)
);



