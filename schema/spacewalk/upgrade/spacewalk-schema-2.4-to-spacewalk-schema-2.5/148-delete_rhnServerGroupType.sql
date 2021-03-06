
-- rhnServerGroup and dependencies

SELECT .clear_log_id();

DELETE FROM rhnOrgExtGroupMapping
  WHERE server_group_id in (
    SELECT id
      FROM rhnServerGroup
      WHERE group_type = (
        SELECT id
          FROM rhnServerGroupType
          WHERE label = 'provisioning_entitled'
      )
  );

DELETE FROM rhnRegTokenGroups
  WHERE server_group_id in (
    SELECT id
      FROM rhnServerGroup
      WHERE group_type = (
        SELECT id
          FROM rhnServerGroupType
          WHERE label = 'provisioning_entitled'
      )
  );

DELETE FROM rhnServerGroupMembers
  WHERE server_group_id in (
    SELECT id
      FROM rhnServerGroup
      WHERE group_type = (
        SELECT id
          FROM rhnServerGroupType
          WHERE label = 'provisioning_entitled'
      )
  );

DELETE FROM rhnSnapshotServerGroup
  WHERE server_group_id in (
    SELECT id
      FROM rhnServerGroup
      WHERE group_type = (
        SELECT id
          FROM rhnServerGroupType
          WHERE label = 'provisioning_entitled'
      )
  );

DELETE FROM rhnUserDefaultSystemGroups
  WHERE system_group_id in (
    SELECT id
      FROM rhnServerGroup
      WHERE group_type = (
        SELECT id
          FROM rhnServerGroupType
          WHERE label = 'provisioning_entitled'
      )
  );

DELETE FROM rhnUserServerGroupPerms
  WHERE server_group_id in (
    SELECT id
      FROM rhnServerGroup
      WHERE group_type = (
        SELECT id
          FROM rhnServerGroupType
          WHERE label = 'provisioning_entitled'
      )
  );

DELETE FROM rhnServerGroup
  WHERE group_type = (
    SELECT id
      FROM rhnServerGroupType
      WHERE label = 'provisioning_entitled'
  );

-- rhnServerGroupType and dependencies

DELETE FROM rhnRegTokenEntitlement
  WHERE server_group_type_id = (
    SELECT id
      FROM rhnServerGroupType
      WHERE label = 'provisioning_entitled'
  );

UPDATE rhnServerGroupTypeFeature
  SET server_group_type_id = (
    SELECT id
      FROM rhnServerGroupType
      WHERE label = 'enterprise_entitled'
  )
  WHERE server_group_type_id = (
    SELECT id
      FROM rhnServerGroupType
      WHERE label = 'provisioning_entitled'
  );

DELETE FROM rhnServerServerGroupArchCompat
  WHERE server_group_type = (
    SELECT id
      FROM rhnServerGroupType
      WHERE label = 'provisioning_entitled'
  );

DELETE FROM rhnSGTypeBaseAddonCompat
  WHERE addon_id = (
    SELECT id
      FROM rhnServerGroupType
      WHERE label = 'provisioning_entitled'
  );

DELETE FROM rhnServerGroupType
  WHERE label = 'provisioning_entitled';
