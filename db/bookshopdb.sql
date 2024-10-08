PGDMP  4             
        |         	   book_shop    16.3    16.3 4    /           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            0           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            1           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            2           1262    32814 	   book_shop    DATABASE     �   CREATE DATABASE book_shop WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'English_United States.1252';
    DROP DATABASE book_shop;
                postgres    false            �            1255    32899 &   add_to_cart(integer, integer, integer)    FUNCTION     �  CREATE FUNCTION public.add_to_cart(p_user_id integer, p_book_id integer, p_quantity integer) RETURNS void
    LANGUAGE plpgsql
    AS $$
DECLARE
    v_cart_id INT;
    v_item_id INT;
BEGIN
    -- Lấy cart_id từ cart của user
    SELECT cart_id INTO v_cart_id FROM cart WHERE user_id = p_user_id;

    -- Nếu không có giỏ hàng, tạo mới giỏ hàng
    IF v_cart_id IS NULL THEN
        INSERT INTO cart (user_id) VALUES (p_user_id) RETURNING cart_id INTO v_cart_id;
    END IF;

    -- Kiểm tra xem sách đã có trong giỏ hàng chưa
    SELECT item_id INTO v_item_id FROM cart_items WHERE cart_id = v_cart_id AND book_id = p_book_id;

    IF FOUND THEN
        -- Nếu có thì cập nhật số lượng
        UPDATE cart_items SET quantity = quantity + p_quantity WHERE item_id = v_item_id;
    ELSE
        -- Nếu chưa có thì thêm mới vào giỏ hàng
        INSERT INTO cart_items (cart_id, book_id, quantity) VALUES (v_cart_id, p_book_id, p_quantity);
    END IF;
END;
$$;
 \   DROP FUNCTION public.add_to_cart(p_user_id integer, p_book_id integer, p_quantity integer);
       public          postgres    false            �            1255    32901    confirm_cart(integer)    FUNCTION     B  CREATE FUNCTION public.confirm_cart(p_user_id integer) RETURNS void
    LANGUAGE plpgsql
    AS $$
DECLARE
    v_cart_id INT;
    v_order_id INT;
BEGIN
    -- Lấy cart_id từ cart của user
    SELECT cart_id INTO v_cart_id FROM cart WHERE user_id = p_user_id;

    -- Tạo đơn hàng mới
    INSERT INTO orders(user_id) VALUES (p_user_id) RETURNING order_id INTO v_order_id;

    -- Chuyển các sản phẩm từ giỏ hàng sang order_items
    INSERT INTO order_items(order_id, book_id, quantity, price)
    SELECT v_order_id, ci.book_id, ci.quantity, b.book_price
    FROM cart_items ci
    JOIN books b ON ci.book_id = b.book_id
    WHERE ci.cart_id = v_cart_id;

    -- Xóa giỏ hàng sau khi xác nhận
    DELETE FROM cart_items WHERE cart_id = v_cart_id;
    DELETE FROM cart WHERE cart_id = v_cart_id;
END;
$$;
 6   DROP FUNCTION public.confirm_cart(p_user_id integer);
       public          postgres    false            �            1255    32903    confirm_order(integer)    FUNCTION       CREATE FUNCTION public.confirm_order(p_order_id integer) RETURNS void
    LANGUAGE plpgsql
    AS $$
BEGIN
    -- Cập nhật trạng thái đơn hàng thành "Confirmed"
    UPDATE orders SET status = 'Confirmed' 
	WHERE order_id = p_order_id AND status = 'Pending';
END;
$$;
 8   DROP FUNCTION public.confirm_order(p_order_id integer);
       public          postgres    false            �            1255    32904     mark_order_as_completed(integer)    FUNCTION     y  CREATE FUNCTION public.mark_order_as_completed(p_order_id integer) RETURNS void
    LANGUAGE plpgsql
    AS $$
BEGIN
    -- Cập nhật trạng thái đơn hàng thành "Completed"
    UPDATE orders
    SET status = 'Completed', completed_at = CURRENT_TIMESTAMP
    WHERE order_id = p_order_id AND status = 'Confirmed';
    
    -- Cập nhật số lượng sách đã bán và giảm số lượng sách tồn kho
    UPDATE books b
    SET book_sold = book_sold + oi.quantity,
        book_quantity = book_quantity - oi.quantity
    FROM order_items oi
    WHERE b.book_id = oi.book_id
    AND oi.order_id = p_order_id;
END;
$$;
 B   DROP FUNCTION public.mark_order_as_completed(p_order_id integer);
       public          postgres    false            �            1255    32900 +   remove_from_cart(integer, integer, integer)    FUNCTION     u  CREATE FUNCTION public.remove_from_cart(p_user_id integer, p_book_id integer, p_quantity integer) RETURNS void
    LANGUAGE plpgsql
    AS $$
DECLARE
    v_cart_id INT;
    v_item_id INT;
    v_current_quantity INT;
BEGIN
    -- Lấy cart_id từ cart của user
    SELECT cart_id INTO v_cart_id FROM cart WHERE user_id = p_user_id;

    -- Kiểm tra xem sản phẩm có trong giỏ hàng không và lấy số lượng hiện tại
    SELECT item_id, quantity INTO v_item_id, v_current_quantity 
    FROM cart_items 
    WHERE cart_id = v_cart_id AND book_id = p_book_id;

    IF FOUND THEN
        IF v_current_quantity > p_quantity THEN
            -- Nếu số lượng cần xóa nhỏ hơn số lượng hiện tại, thì chỉ cập nhật số lượng
            UPDATE cart_items 
            SET quantity = quantity - p_quantity 
            WHERE item_id = v_item_id;
        ELSE
            -- Nếu số lượng cần xóa lớn hơn hoặc bằng số lượng hiện tại, thì xóa sản phẩm khỏi giỏ hàng
            DELETE FROM cart_items WHERE item_id = v_item_id;
        END IF;
    END IF;
END;
$$;
 a   DROP FUNCTION public.remove_from_cart(p_user_id integer, p_book_id integer, p_quantity integer);
       public          postgres    false            �            1259    32910    books_book_id_seq    SEQUENCE     z   CREATE SEQUENCE public.books_book_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 (   DROP SEQUENCE public.books_book_id_seq;
       public          postgres    false            �            1259    32825    books    TABLE     �  CREATE TABLE public.books (
    book_id integer DEFAULT nextval('public.books_book_id_seq'::regclass) NOT NULL,
    book_title character varying(255) NOT NULL,
    book_author character varying(255) NOT NULL,
    book_price numeric(10,2) NOT NULL,
    book_quantity integer NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    book_sold integer DEFAULT 0
);
    DROP TABLE public.books;
       public         heap    postgres    false    224            �            1259    32868    cart    TABLE     �   CREATE TABLE public.cart (
    cart_id integer NOT NULL,
    user_id integer NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);
    DROP TABLE public.cart;
       public         heap    postgres    false            �            1259    32867    cart_cart_id_seq    SEQUENCE     �   CREATE SEQUENCE public.cart_cart_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 '   DROP SEQUENCE public.cart_cart_id_seq;
       public          postgres    false    220            3           0    0    cart_cart_id_seq    SEQUENCE OWNED BY     E   ALTER SEQUENCE public.cart_cart_id_seq OWNED BY public.cart.cart_id;
          public          postgres    false    219            �            1259    32881 
   cart_items    TABLE     �   CREATE TABLE public.cart_items (
    item_id integer NOT NULL,
    cart_id integer NOT NULL,
    book_id integer NOT NULL,
    quantity integer NOT NULL
);
    DROP TABLE public.cart_items;
       public         heap    postgres    false            �            1259    32880    cart_items_item_id_seq    SEQUENCE     �   CREATE SEQUENCE public.cart_items_item_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 -   DROP SEQUENCE public.cart_items_item_id_seq;
       public          postgres    false    222            4           0    0    cart_items_item_id_seq    SEQUENCE OWNED BY     Q   ALTER SEQUENCE public.cart_items_item_id_seq OWNED BY public.cart_items.item_id;
          public          postgres    false    221            �            1259    32912    order_items_item_id_seq    SEQUENCE     �   CREATE SEQUENCE public.order_items_item_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 .   DROP SEQUENCE public.order_items_item_id_seq;
       public          postgres    false            �            1259    32852    order_items    TABLE     �   CREATE TABLE public.order_items (
    item_id integer DEFAULT nextval('public.order_items_item_id_seq'::regclass) NOT NULL,
    order_id bigint NOT NULL,
    book_id bigint NOT NULL,
    quantity integer NOT NULL,
    price numeric(10,2) NOT NULL
);
    DROP TABLE public.order_items;
       public         heap    postgres    false    226            �            1259    32911    orders_order_id_seq    SEQUENCE     |   CREATE SEQUENCE public.orders_order_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 *   DROP SEQUENCE public.orders_order_id_seq;
       public          postgres    false            �            1259    32840    orders    TABLE     N  CREATE TABLE public.orders (
    order_id integer DEFAULT nextval('public.orders_order_id_seq'::regclass) NOT NULL,
    user_id bigint NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    status character varying(50) DEFAULT 'Pending'::character varying,
    completed_at timestamp without time zone
);
    DROP TABLE public.orders;
       public         heap    postgres    false    225            �            1259    32909    users_user_id_seq    SEQUENCE     z   CREATE SEQUENCE public.users_user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 (   DROP SEQUENCE public.users_user_id_seq;
       public          postgres    false            �            1259    32815    users    TABLE     I  CREATE TABLE public.users (
    user_id integer DEFAULT nextval('public.users_user_id_seq'::regclass) NOT NULL,
    user_name character varying(50) NOT NULL,
    user_email character varying(100) NOT NULL,
    user_password character varying(150) NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);
    DROP TABLE public.users;
       public         heap    postgres    false    223            w           2604    32917    cart cart_id    DEFAULT     l   ALTER TABLE ONLY public.cart ALTER COLUMN cart_id SET DEFAULT nextval('public.cart_cart_id_seq'::regclass);
 ;   ALTER TABLE public.cart ALTER COLUMN cart_id DROP DEFAULT;
       public          postgres    false    220    219    220            y           2604    32918    cart_items item_id    DEFAULT     x   ALTER TABLE ONLY public.cart_items ALTER COLUMN item_id SET DEFAULT nextval('public.cart_items_item_id_seq'::regclass);
 A   ALTER TABLE public.cart_items ALTER COLUMN item_id DROP DEFAULT;
       public          postgres    false    222    221    222            "          0    32825    books 
   TABLE DATA           s   COPY public.books (book_id, book_title, book_author, book_price, book_quantity, created_at, book_sold) FROM stdin;
    public          postgres    false    216   �K       &          0    32868    cart 
   TABLE DATA           <   COPY public.cart (cart_id, user_id, created_at) FROM stdin;
    public          postgres    false    220   �L       (          0    32881 
   cart_items 
   TABLE DATA           I   COPY public.cart_items (item_id, cart_id, book_id, quantity) FROM stdin;
    public          postgres    false    222   �L       $          0    32852    order_items 
   TABLE DATA           R   COPY public.order_items (item_id, order_id, book_id, quantity, price) FROM stdin;
    public          postgres    false    218   M       #          0    32840    orders 
   TABLE DATA           U   COPY public.orders (order_id, user_id, created_at, status, completed_at) FROM stdin;
    public          postgres    false    217   SM       !          0    32815    users 
   TABLE DATA           Z   COPY public.users (user_id, user_name, user_email, user_password, created_at) FROM stdin;
    public          postgres    false    215   �M       5           0    0    books_book_id_seq    SEQUENCE SET     ?   SELECT pg_catalog.setval('public.books_book_id_seq', 5, true);
          public          postgres    false    224            6           0    0    cart_cart_id_seq    SEQUENCE SET     >   SELECT pg_catalog.setval('public.cart_cart_id_seq', 5, true);
          public          postgres    false    219            7           0    0    cart_items_item_id_seq    SEQUENCE SET     D   SELECT pg_catalog.setval('public.cart_items_item_id_seq', 9, true);
          public          postgres    false    221            8           0    0    order_items_item_id_seq    SEQUENCE SET     E   SELECT pg_catalog.setval('public.order_items_item_id_seq', 5, true);
          public          postgres    false    226            9           0    0    orders_order_id_seq    SEQUENCE SET     A   SELECT pg_catalog.setval('public.orders_order_id_seq', 3, true);
          public          postgres    false    225            :           0    0    users_user_id_seq    SEQUENCE SET     ?   SELECT pg_catalog.setval('public.users_user_id_seq', 4, true);
          public          postgres    false    223            �           2606    32832    books books_pkey 
   CONSTRAINT     S   ALTER TABLE ONLY public.books
    ADD CONSTRAINT books_pkey PRIMARY KEY (book_id);
 :   ALTER TABLE ONLY public.books DROP CONSTRAINT books_pkey;
       public            postgres    false    216            �           2606    32888 )   cart_items cart_items_cart_id_book_id_key 
   CONSTRAINT     p   ALTER TABLE ONLY public.cart_items
    ADD CONSTRAINT cart_items_cart_id_book_id_key UNIQUE (cart_id, book_id);
 S   ALTER TABLE ONLY public.cart_items DROP CONSTRAINT cart_items_cart_id_book_id_key;
       public            postgres    false    222    222            �           2606    32886    cart_items cart_items_pkey 
   CONSTRAINT     ]   ALTER TABLE ONLY public.cart_items
    ADD CONSTRAINT cart_items_pkey PRIMARY KEY (item_id);
 D   ALTER TABLE ONLY public.cart_items DROP CONSTRAINT cart_items_pkey;
       public            postgres    false    222            �           2606    32874    cart cart_pkey 
   CONSTRAINT     Q   ALTER TABLE ONLY public.cart
    ADD CONSTRAINT cart_pkey PRIMARY KEY (cart_id);
 8   ALTER TABLE ONLY public.cart DROP CONSTRAINT cart_pkey;
       public            postgres    false    220            �           2606    32856    order_items order_items_pkey 
   CONSTRAINT     _   ALTER TABLE ONLY public.order_items
    ADD CONSTRAINT order_items_pkey PRIMARY KEY (item_id);
 F   ALTER TABLE ONLY public.order_items DROP CONSTRAINT order_items_pkey;
       public            postgres    false    218            �           2606    32846    orders orders_pkey 
   CONSTRAINT     V   ALTER TABLE ONLY public.orders
    ADD CONSTRAINT orders_pkey PRIMARY KEY (order_id);
 <   ALTER TABLE ONLY public.orders DROP CONSTRAINT orders_pkey;
       public            postgres    false    217            {           2606    32820    users users_pkey 
   CONSTRAINT     S   ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (user_id);
 :   ALTER TABLE ONLY public.users DROP CONSTRAINT users_pkey;
       public            postgres    false    215            }           2606    32824    users users_user_email_key 
   CONSTRAINT     [   ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_user_email_key UNIQUE (user_email);
 D   ALTER TABLE ONLY public.users DROP CONSTRAINT users_user_email_key;
       public            postgres    false    215                       2606    32822    users users_user_name_key 
   CONSTRAINT     Y   ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_user_name_key UNIQUE (user_name);
 C   ALTER TABLE ONLY public.users DROP CONSTRAINT users_user_name_key;
       public            postgres    false    215            �           2606    32894 "   cart_items cart_items_book_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.cart_items
    ADD CONSTRAINT cart_items_book_id_fkey FOREIGN KEY (book_id) REFERENCES public.books(book_id) ON DELETE CASCADE;
 L   ALTER TABLE ONLY public.cart_items DROP CONSTRAINT cart_items_book_id_fkey;
       public          postgres    false    4737    216    222            �           2606    32889 "   cart_items cart_items_cart_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.cart_items
    ADD CONSTRAINT cart_items_cart_id_fkey FOREIGN KEY (cart_id) REFERENCES public.cart(cart_id) ON DELETE CASCADE;
 L   ALTER TABLE ONLY public.cart_items DROP CONSTRAINT cart_items_cart_id_fkey;
       public          postgres    false    222    4743    220            �           2606    32875    cart cart_user_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.cart
    ADD CONSTRAINT cart_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(user_id) ON DELETE CASCADE;
 @   ALTER TABLE ONLY public.cart DROP CONSTRAINT cart_user_id_fkey;
       public          postgres    false    4731    220    215            �           2606    32862 $   order_items order_items_book_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.order_items
    ADD CONSTRAINT order_items_book_id_fkey FOREIGN KEY (book_id) REFERENCES public.books(book_id) ON DELETE CASCADE;
 N   ALTER TABLE ONLY public.order_items DROP CONSTRAINT order_items_book_id_fkey;
       public          postgres    false    4737    216    218            �           2606    32857 %   order_items order_items_order_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.order_items
    ADD CONSTRAINT order_items_order_id_fkey FOREIGN KEY (order_id) REFERENCES public.orders(order_id) ON DELETE CASCADE;
 O   ALTER TABLE ONLY public.order_items DROP CONSTRAINT order_items_order_id_fkey;
       public          postgres    false    218    4739    217            �           2606    32847    orders orders_user_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.orders
    ADD CONSTRAINT orders_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(user_id) ON DELETE CASCADE;
 D   ALTER TABLE ONLY public.orders DROP CONSTRAINT orders_user_id_fkey;
       public          postgres    false    215    4731    217            "   �   x�}�Mn�0����s�X�ɟ�U�HE[K6&�[�Aƨ����d9�y�~5l�w�6:�&��;?ll$|��2Eh�1�PBՕ�+aP��+)y�6�i@��g��%�s9w�"��{|<S�kZ,[��ٌ�ͷ�����q��o�lp ��-Y�i���a�)�	?�� ����Sp��͇�?��8���զk��N��T�]L���c�Xz      &      x������ � �      (      x������ � �      $   (   x�3�4�4�4�г��2r�8M8�AS�8����� ��       #   b   x�U�;
�@�zu
_����B$��s�-c�!�`r�*�~+��z*�EUu�V��z|����#� �y�khM�~�L�� �~ۼ��u���*"?��      !   �   x�e͹�0 ��>����j�<�6Q�I\*i<���zz��˷~���ܰ���Y�Ϊ��3�S�ck�3�~��h���\�h>�/͚Bn]%�I�͡5�&�J�j ��aA�t��7hQA���>����B��WX�I�ʪc�}\g�cy��i1��NO�Ev;��U!�K�|�B�I�S�A;�z�C"     