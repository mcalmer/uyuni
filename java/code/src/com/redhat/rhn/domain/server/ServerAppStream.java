/*
 * Copyright (c) 2024--2025 SUSE LLC
 *
 * This software is licensed to you under the GNU General Public License,
 * version 2 (GPLv2). There is NO WARRANTY for this software, express or
 * implied, including the implied warranties of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. You should have received a copy of GPLv2
 * along with this software; if not, see
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.txt.
 */
package com.redhat.rhn.domain.server;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import java.io.Serializable;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "suseServerAppstream")
public class ServerAppStream implements Serializable  {
    @Id
    @GeneratedValue(generator = "appstreams_servermodule_seq")
    @GenericGenerator(
            name = "appstreams_servermodule_seq",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "suse_as_servermodule_seq"),
                    @Parameter(name = "increment_size", value = "1")
            })
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String stream;

    @Column(nullable = false)
    private String version;

    @Column(nullable = false)
    private String context;

    @Column(nullable = false)
    private String arch;

    @ManyToOne
    @JoinColumn(name = "server_id")
    private Server server;

    /**
     * Constructs a ServerAppStream instance.
     */
    public ServerAppStream() { }

    /**
     * Constructs a ServerAppStream based on a provided Map containing NSVCA keys.
     *
     * @param serverIn The server
     * @param nsvca A Map containing NSVCA keys: "name", "stream", "version", "context", "architecture".
     */
    public ServerAppStream(Server serverIn, Map<String, String> nsvca) {
        this.setName(nsvca.get("name"));
        this.setStream(nsvca.get("stream"));
        this.setVersion(nsvca.get("version"));
        this.setContext(nsvca.get("context"));
        this.setArch(nsvca.get("architecture"));
        this.server = serverIn;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long idIn) {
        id = idIn;
    }

    public String getName() {
        return name;
    }

    public void setName(String nameIn) {
        name = nameIn;
    }

    public String getStream() {
        return stream;
    }

    public void setStream(String streamIn) {
        stream = streamIn;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String versionIn) {
        version = versionIn;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String contextIn) {
        context = contextIn;
    }

    public String getArch() {
        return arch;
    }

    public void setArch(String archIn) {
        arch = archIn;
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server serverIn) {
        server = serverIn;
    }
}
