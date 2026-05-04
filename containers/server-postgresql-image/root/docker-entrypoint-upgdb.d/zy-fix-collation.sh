#!/bin/bash
#
# Copyright (c) 2026 SUSE LLC
#
# This software is licensed to you under the GNU General Public License,
# version 2 (GPLv2). There is NO WARRANTY for this software, express or
# implied, including the implied warranties of MERCHANTABILITY or FITNESS
# FOR A PARTICULAR PURPOSE. You should have received a copy of GPLv2
# along with this software; if not, see
# http://www.gnu.org/licenses/old-licenses/gpl-2.0.txt.
#

run_sql() {
  su postgres -c "psql -t \"$@\""
}

for DB in $(echo "SELECT d.datname database FROM pg_database as d JOIN pg_collation c ON c.collname = d.datcollate WHERE d.datcollversion <> pg_collation_actual_version(c.oid);" | run_sql); do
  echo "ALTER DATABASE $DB REFRESH COLLATION VERSION;" | run_sql
done
