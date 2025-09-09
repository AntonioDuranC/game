CREATE TABLE public.users (
      id serial PRIMARY KEY ,
      username VARCHAR(255) NOT NULL,
      password VARCHAR(255) NOT NULL,
      email VARCHAR(200) UNIQUE NOT NULL,
      created_at TIMESTAMP DEFAULT NOW(),
      CONSTRAINT UK_user_username UNIQUE (username)
);

CREATE TABLE public.user_roles (
    user_id INTEGER NOT NULL,
    roles VARCHAR(255) NOT NULL,
    PRIMARY KEY (user_id, roles),
    CONSTRAINT FK_user_roles_user FOREIGN KEY (user_id) REFERENCES public.users(id)
);