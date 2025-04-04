# -*- coding: utf-8 -*-
"""
SUSE Multi-Linux Manager master_tops module
-------------------------------

This module provides the base states top information from SUSE Multi-Linux Manager.

The top information returned by this module is merged by Salt with the
user custom data provided in /srv/salt/top.sls file.

.. code-block:: yaml

    master_tops:
      mgr_master_tops: True
"""

# Import python libs
from __future__ import absolute_import
import logging

# Define the module's virtual name
__virtualname__ = "mgr_master_tops"

log = logging.getLogger(__name__)

MANAGER_BASE_TOP = [
    "channels",
    "certs",
    "packages",
    "custom",
    "custom_groups",
    "custom_org",
    "formulas",
    "services.salt-minion",
    "services.docker",
    "services.kiwi-image-server",
    "ansible",
    "switch_to_bundle.mgr_switch_to_venv_minion",
]


# pylint: disable-next=invalid-name
def __virtual__():
    """
    Ensure the module name.
    """
    return __virtualname__


def top(**kwargs):
    """
    Returns the SUSE Multi-Linux Manager top state information of a minion
    for the `base` salt environment.
    """
    env = kwargs["opts"].get("environment") or kwargs["opts"].get("saltenv")
    if env in [None, "base"]:
        log.debug(
            'Loading SUSE Multi-Linux Manager TOP state information for the "base" environment'
        )
        return {"base": MANAGER_BASE_TOP}
    return None
