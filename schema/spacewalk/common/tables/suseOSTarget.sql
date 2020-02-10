--
-- Copyright (c) 2011 Novell
--
-- This software is licensed to you under the GNU General Public License,
-- version 2 (GPLv2). There is NO WARRANTY for this software, express or
-- implied, including the implied warranties of MERCHANTABILITY or FITNESS
-- FOR A PARTICULAR PURPOSE. You should have received a copy of GPLv2
-- along with this software; if not, see
-- http://www.gnu.org/licenses/old-licenses/gpl-2.0.txt.
--
--


create table
suseOSTarget
(
    id            NUMERIC        not null PRIMARY KEY,
    os            VARCHAR(200) not null
                  CONSTRAINT suseostarget_os_uq UNIQUE,
    target        VARCHAR(100) not null,
    channel_arch_id  NUMERIC
                  CONSTRAINT suse_ostarget_caid_fk
                  REFERENCES rhnChannelArch (id),
    created   TIMESTAMPTZ
                  DEFAULT (current_timestamp) NOT NULL,
    modified  TIMESTAMPTZ
                  DEFAULT (current_timestamp) NOT NULL
);

CREATE SEQUENCE suse_ostarget_id_seq START WITH 100;

