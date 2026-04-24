--
-- Copyright (c) 2026 SUSE LLC
--
-- This software is licensed to you under the GNU General Public License,
-- version 2 (GPLv2). There is NO WARRANTY for this software, express or
-- implied, including the implied warranties of MERCHANTABILITY or FITNESS
-- FOR A PARTICULAR PURPOSE. You should have received a copy of GPLv2
-- along with this software; if not, see
-- http://www.gnu.org/licenses/old-licenses/gpl-2.0.txt.
--

-- Migration: Add scap_content_id and tailoring_file_id to rhnActionScap
-- This enables per-ID directory file resolution for both policy-based and one-off scans

ALTER TABLE rhnActionScap
ADD COLUMN IF NOT EXISTS scap_content_id BIGINT
CONSTRAINT fk_scap_content
REFERENCES suseScapContent(id)
ON DELETE SET NULL;

ALTER TABLE rhnActionScap
ADD COLUMN IF NOT EXISTS tailoring_file_id BIGINT
CONSTRAINT fk_tailoring_file
REFERENCES suseScapTailoringFile(id)
ON DELETE SET NULL;

CREATE INDEX IF NOT EXISTS rhn_act_scap_content_idx
ON rhnActionScap(scap_content_id);

CREATE INDEX IF NOT EXISTS rhn_act_scap_tailoring_idx
ON rhnActionScap(tailoring_file_id);
