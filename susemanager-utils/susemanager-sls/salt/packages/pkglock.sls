{% if pillar.get('param_pkgs') %}
pkg_locked:
  pkg.held:
    - replace: True
    - pkgs:
{%- for pkg, arch, version in pillar.get('param_pkgs', []) %}
        - {{ pkg }}
{%- endfor %}
{% endif %}
