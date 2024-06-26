#!/usr/bin/python
#  pylint: disable=missing-module-docstring,invalid-name
#
# Copyright (c) 2008--2013 Red Hat, Inc.
#
# This software is licensed to you under the GNU General Public License,
# version 2 (GPLv2). There is NO WARRANTY for this software, express or
# implied, including the implied warranties of MERCHANTABILITY or FITNESS
# FOR A PARTICULAR PURPOSE. You should have received a copy of GPLv2
# along with this software; if not, see
# http://www.gnu.org/licenses/old-licenses/gpl-2.0.txt.
#
# Red Hat trademarks are not licensed under GPLv2. No permission is
# granted to use or replicate Red Hat trademarks that are incorporated
# in this software or its documentation.
#

from spacewalk.common import rhnLog
from spacewalk.server import rhnImport

rhnLog.initLOG(level=4)

# pylint: disable-next=redefined-builtin
dir = "spacewalk/server/handlers"

for i in range(2):
    for iface in ["rpcClasses", "getHandler"]:
        m = rhnImport.load(dir, interface_signature=iface)
