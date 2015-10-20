#!/usr/bin/env bash
source "$(dirname $0)/common.sh" || exit -1
checkTravisSecure

if (($# < 1)); then
  die "Please provide the settings file you wish to write to"
fi
declare -r SETTINGS_FILE="$1"; shift

cat <<EOF > "$SETTINGS_FILE"
<settings>
  <servers>
    <server>
      <id>ossrh</id>
      <username>$SONATYPE_USER</username>
      <password>$SONATYPE_PASSWD</password>
    </server>
  </servers>

  <profiles>
    <profile>
      <id>ossrh</id>
      <activation>
        <activeByDefault>true</activeByDefault>
      </activation>
      <properties>
        <gpg.executable>gpg2</gpg.executable>
        <gpg.keyname>$GPG_USER</gpg.keyname>
        <gpg.passphrase>$GPG_PASSWD</gpg.passphrase>
      </properties>
    </profile>
  </profiles>
</settings>
EOF
