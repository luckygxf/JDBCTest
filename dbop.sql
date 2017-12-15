create PROCEDURE pr_add(a int, b int)
BEGIN
	DECLARE c int;
	if a is null THEN
		set a = 0;
	end if;
	if b is null THEN
		set b = 0;
	end if;
	set c = a + b;
	select c as sum;
end;