#
# spec file for package supportutils-plugin-susemanager-proxy
#
# Copyright (c) 2024 SUSE LLC
#
# All modifications and additions to the file contributed by third parties
# remain the property of their copyright owners, unless otherwise agreed
# upon. The license for this file, and modifications and additions to the
# file, is the same license as for the pristine package itself (unless the
# license for the pristine package is not an Open Source License, in which
# case the license is the MIT License). An "Open Source License" is a
# license that conforms to the Open Source Definition (Version 1.9)
# published by the Open Source Initiative.

# Please submit bugfixes or comments via https://bugs.opensuse.org/
#


Name:           supportutils-plugin-susemanager-proxy
Version:        5.1.3
Release:        0
Summary:        Supportconfig Plugin for SUSE Manager Proxy
License:        GPL-2.0-only
# FIXME: use correct group or remove it, see "https://en.opensuse.org/openSUSE:Package_group_guidelines"
Group:          Documentation/SuSE
Source:         %{name}-%{version}.tar.gz
URL:            https://github.com/uyuni-project/uyuni
Requires:       spacewalk-proxy-common
Requires:       supportutils
BuildArch:      noarch

%description
Extends supportconfig functionality to include system information about
SUSE Manager Proxy. The supportconfig saves the plugin output to
plugin-susemanagerproxy.txt.

%prep
%setup -q

%build
gzip -9f susemanagerproxy-plugin.8

%install
pwd;ls -la
install -d %{buildroot}%{_prefix}/lib/supportconfig/plugins
install -d %{buildroot}%{_mandir}/man8
install -d %{buildroot}/sbin
install -m 0544 susemanagerproxy %{buildroot}%{_prefix}/lib/supportconfig/plugins
install -m 0644 susemanagerproxy-plugin.8.gz %{buildroot}%{_mandir}/man8/susemanagerproxy-plugin.8.gz

%files
%defattr(-,root,root)
%license COPYING.GPLv2
%{_prefix}/lib/supportconfig
%{_prefix}/lib/supportconfig/plugins
%{_prefix}/lib/supportconfig/plugins/susemanagerproxy
%{_mandir}/man8/susemanagerproxy-plugin.8%{?ext_man}

%changelog
