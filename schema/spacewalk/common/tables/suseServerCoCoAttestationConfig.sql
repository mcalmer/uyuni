--
-- Copyright (c) 2024 SUSE
--
-- This software is licensed to you under the GNU General Public License,
-- version 2 (GPLv2). There is NO WARRANTY for this software, express or
-- implied, including the implied warranties of MERCHANTABILITY or FITNESS
-- FOR A PARTICULAR PURPOSE. You should have received a copy of GPLv2
-- along with this software; if not, see
-- http://www.gnu.org/licenses/old-licenses/gpl-2.0.txt.
--

CREATE TABLE suseServerCoCoAttestationConfig
(
	server_id NUMERIC NOT NULL
                    CONSTRAINT suse_srvcocoatt_cnf_sid_pk PRIMARY KEY,
        CONSTRAINT suse_srvcocoatt_cnf_sid_fk FOREIGN KEY (server_id) REFERENCES rhnServer (id),
	enabled   BOOLEAN NOT NULL DEFAULT FALSE,
	env_type  NUMERIC NULL
);
