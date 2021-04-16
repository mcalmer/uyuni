create or replace function rhn_virtinst_info_iud_trig_fun() returns trigger as
$$
begin
        if tg_op='INSERT' or (tg_op='UPDATE' and old.instance_type is distinct from new.instance_type) then
                update suseSCCRegCache
                   set scc_reg_required = 'Y'
                 where server_id = (select virtual_system_id from rhnvirtualinstance WHERE id = new.instance_id);
                return new;
        end if;
        if tg_op='DELETE' then
                update suseSCCRegCache
                   set scc_reg_required = 'Y'
                 where server_id = (select virtual_system_id from rhnvirtualinstance WHERE id = old.instance_id);
                return old;
        end if;
end;
$$ language plpgsql;

drop trigger if exists rhn_virtinst_info_iud_trig;
create trigger
rhn_virtinst_info_iud_trig
after insert or update or delete on rhnVirtualInstanceInfo
for each row
execute procedure rhn_virtinst_info_iud_trig_fun();
