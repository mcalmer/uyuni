# Makefile for spacewalk backend
#

# what is the backend top dir
TOP     = ..

# Specific stuff
SUBDIR  = common
SPACEWALK_FILES	=   __init__ \
	    apache \
            byterange \
            fileutils \
            rhnApache \
            rhnCache \
            rhnConfig \
            rhnException \
            rhnFlags \
            rhnLog \
            rhnMail \
            rhnRepository \
            rhnTB \
            rhnTranslate \
            RPC_Base \
            suseLib \
            repo

SCRIPTS = spacewalk-cfg-get

# check if we can build man pages
DOCBOOK = $(wildcard /usr/bin/docbook2man)

SGMLS   = $(wildcard *.sgml)
MANS    = $(patsubst %.sgml,%.8,$(SGMLS))

BINDIR  = /usr/bin
MANDIR  ?= /usr/man

EXTRA_DIRS = $(MANDIR)/man8 $(BINDIR)

include $(TOP)/Makefile.defs

# install scripts
all :: $(SCRIPTS)
install :: $(SCRIPTS) $(PREFIX)/$(BINDIR)
	$(INSTALL_BIN) $(SCRIPTS) $(PREFIX)/$(BINDIR)

ifneq ($(DOCBOOK),)
# install man pages
all     :: $(MANS)
install :: $(MANS) $(PREFIX)/$(MANDIR)
	$(INSTALL_DATA) $(MANS) $(PREFIX)/$(MANDIR)/man8
endif

%.8 : %.sgml
	$(DOCBOOK) $<

clean :: 
	@rm -fv $(MANS) manpage.*

