# meta-maaxboard

A meta-layer for Embest MaaXBoard.

## How to

### Install Host Yocto Development Env

You should have a linux machine, below instructions show how to setup the env on a Ubuntu:18.04 machine.

```bash
$ sudo apt-get update && sudo apt-get install -y \
        gawk \
        wget \
        git-core \
        diffstat \
        unzip \
        texinfo \
        gcc-multilib \
        build-essential \
        chrpath \
        socat \
        libsdl1.2-dev \
        xterm \
        sed \
        cvs \
        subversion \
        coreutils \
        texi2html \
        docbook-utils \
        python-pysqlite2 \
        help2man \
        make \
        gcc \
        g++ \
        desktop-file-utils \
        libgl1-mesa-dev \
        libglu1-mesa-dev \
        mercurial \
        autoconf \
        automake \
        groff \
        curl \
        lzop \
        asciidoc \
        u-boot-tools \
        cpio \
        sudo \
        locales
```

Install repo

```bash
sudo curl http://commondatastorage.googleapis.com/git-repo-downloads/repo > /usr/bin/repo
sudo chmod a+x /usr/bin/repo
```

### Fetch the source

Download meta layers from NXP

```bash
mkdir imx-yocto-bsp
$ cd imx-yocto-bsp
$ repo init -u https://source.codeaurora.org/external/imx/imx-manifest -b imx-linux-sumo -m imx-4.14.98-2.3.0.xml
```

**WARNING!**
This repo provide by repo owner is not anymore valid due to changed unencrypted Git security protocols. So I needed to edit repo above and then run **repo sync** code

NOW we need to edit **./.repo/manifests/imx-4.14.98-2.3.0.xml** It should look like below:
```
<?xml version="1.0" encoding="UTF-8"?>
<manifest>

  <default sync-j="2"/>

  <remote fetch="https://git.yoctoproject.org" name="yocto"/>
  <remote fetch="https://github.com/Freescale" name="community"/>
  <remote fetch="https://github.com/openembedded" name="oe"/>
  <remote fetch="https://github.com/OSSystems" name="OSSystems"/>
  <remote fetch="https://github.com/meta-qt5"  name="QT5"/>
  <remote fetch="https://source.codeaurora.org/external/imx" name="CAF"/>


  <project remote="yocto" revision="cbb677e9a09d5dad34404a851f7c23aeb5122465" name="poky" path="sources/poky"/>
  <project remote="yocto" revision="86772601e7f6ea188dfaf64097edafc05e15aef3" name="meta-freescale" path="sources/meta-freescale"/>

  <project remote="oe" revision="8760facba1bceb299b3613b8955621ddaa3d4c3f" name="meta-openembedded" path="sources/meta-openembedded"/>

  <project remote="community" revision="70535e13dd2aabbad53243518f4cc5064d284592" name="fsl-community-bsp-base" path="sources/base">
    <linkfile dest="README" src="README"/>
    <linkfile dest="setup-environment" src="setup-environment"/>
  </project>

  <project remote="community" revision="82037216280a39957fb4272581637abec734ad50" name="meta-freescale-3rdparty" path="sources/meta-freescale-3rdparty"/>
  <project remote="community" revision="f7e2216e93aff14ac32728a13637a48df436b7f4" name="meta-freescale-distro" path="sources/meta-freescale-distro"/>

  <project remote="OSSystems" revision="75640e14e325479c076b6272b646be7a239c18aa" name="meta-browser" path="sources/meta-browser" />
  <project remote="QT5" revision="d4e7f73d04e8448d326b6f89908701e304e37d65" name="meta-qt5" path="sources/meta-qt5" />

  <project remote="CAF" revision="e1cab5d0fbb8f586f42cf67519822992c852be4c" name="meta-fsl-bsp-release" path="sources/meta-fsl-bsp-release" >
     <linkfile src="imx/tools/fsl-setup-release.sh" dest="fsl-setup-release.sh"/>
     <linkfile src="imx/README" dest="README-IMXBSP"/>
  </project>

</manifest>
```
then run code block below. Just in case, if you encounter any error like unencrypted Git security protocol, you need to find and change all git repo URLs
with https format instead of git://. For example: from git://github.com/meta-qt5 to https://github.com/meta-qt5

```
$ repo sync
```

Clone this repo

```bash
$ cd sources
$ git clone https://github.com/Avnet/meta-maaxboard.git
```

### Do patch

NXP do some hook / patch according different machine / distro when init a new build. We need do this first.

If you're going to build MaaXBoard

```bash
$ cd imx-yocto-bsp
$ mkdir imx8mqevk
$ DISTRO=fsl-imx-wayland MACHINE=imx8mqevk source fsl-setup-release.sh -b imx8mqevk
$ rm -rf imx8mqevk
```

Or if you're going to build MaaXBoard Mini

```bash
$ cd imx-yocto-bsp
$ mkdir imx8mmevk
$ DISTRO=fsl-imx-wayland MACHINE=imx8mmevk source fsl-setup-release.sh -b imx8mmevk
$ rm -rf imx8mmevk
```

###  Build configure

```bash
$ cd imx-yocto-bsp
# Create the build directory
$ mkdir -p maaxboard/build
# Create the default build conf in maaxboard/build
$ source sources/poky/oe-init-build-env maaxboard/build
```

### Upate local conf

You should update 2 conf file in the build directory(maaxboard/build/conf/):

- local.conf
- bblayers.conf

We provide a sample under /meta-maaxboard/conf:

- local.conf.sample
- bblayers.conf.sample

> NOTE: variable 'BSPDIR' in bblayer.conf should be defined, the value should be the repo init directory. It is imx-yocto-bsp directory in above example

If you're going to build MaaXBoard Mini, you should change the Machine(in local.conf) to:

```ini
MACHINE ??= 'maaxboard-mini-ddr4-2g-sdcard'
```

### Build

```bash
$ cd /path/to/bsp_dir/
$ source sources/poky/oe-init-build-env maaxboard/build

$ bitbake lite-image
```

### Flash sdcard

```bash
# This image will be generated under: /path/to/build/tmp/deploy/images/maaxboard-ddr4-2g-sdcard/lite-image-maaxboard-ddr4-2g-sdcard-20200515065319.rootfs.sdcard.bz2
# unzip the bz2 file
$ bunzip2 lite-image-maaxboard-*.rootfs.sdcard.bz2
## dd
$ sudo dd if=lite-image-maaxboard-ddr4-2g-sdcard-20200515065319.rootfs.sdcard of=/dev/sda bs=10M conv=fsync
$ sync
```

### Power On

The default login is user 'root' with the password 'avnet'

## Configuration

- Distor: 'fsl-imx-wayland-lite'
    - meta-maaxboard/conf/distro/fsl-imx-wayland-lite.conf
- Image: "lite-image"
    - meta-maaxboard/images/lite-image.bb
- Machine:
    - MaaXBoard: maaxboard-ddr4-2g-sdcard
    - MaaXBoard Mini: maaxboard-mini-ddr4-2g-sdcard

## Customization

- You could add more packages in the image recipe: meta-maaxboard/images/lite-image.bb
- Distro features: meta-maaxboard/conf/distro/fsl-imx-wayland-lite.conf
- Machine features: meta-maaxboard/conf/machine/maaxboard-ddr4-2g-sdcard.conf


## Setup a Debian Repository

Here we want to show you how to setup a Debian Repository. In this way, you could use `apt-get install` to install packages that you built in Yocto.

### Host Setup

This is the host machine that you build the Yocto images. If you want to setup a Debian repository, you should also install a web server here.

```bash
$ sudo apt install nginx
```

#### Build Packages

Before using `apt-get install` a package, you should first build it. Let's take nano for example.

```bash
$ cd /path/to/bsp_dir/
$ source sources/poky/oe-init-build-env maaxboard/build

$ bitbake nano
```

#### Generate Packages index files

After build the packages, you should generate the package index files for apt-get to search.

First, change to the deb directory:

```bash
$ cd /home/build/maaxboard/maaxboard-yocto/maaxboard/build/tmp/deploy/deb
$ ls
aarch64  aarch64-mx8m  all   maaxboard_ddr4_2g_sdcard
```
Add a script called dpkg-scan.sh

```bash
$ nano dpkg-scan.sh
```

Add

```bash
#!/bin/bash

ls -d */  | sed 's/\///' | cat | while IFS=' ' read -r item
do
echo "[$item] - Scan Packages and generate Packages.gz"
dpkg-scanpackages ${item} | gzip > ${item}/Packages.gz
done
```

```bash
$ sudo chmod +x ./dpkg-scan.sh
```

Exec ./dpkg-scan.sh everytime you build a new package:

```bash
./dpkg-scan.sh
```

#### Config web server

```bash
sudo nano /etc/nginx/sites-available/deb
```

Add

```
server {
    listen 80 default_server;
    server_name yocto_deb_packages;
    root /home/build/maaxboard/maaxboard-yocto/maaxboard/build/tmp/deploy/deb/;

    location / {
        autoindex on;
    }
}
```

Enable website

```bash
# Disable the nginx default site
$ sudo rm /etc/nginx/sites-enabled/default
$ sudo ln -s /etc/nginx/sites-available/deb /etc/nginx/sites-enabled/deb
```

#### Start / Stop nginx

```bash
$ sudo systemctl restart nginx
```

In your client web browser, check the website:

```
http://192.168.2.58/
```

### MaaXBoard Config

#### Add Sources List

```bash
$ sudo nano /etc/apt/sources.list
```

Add

```
deb http://192.168.2.58/ aarch64/
deb http://192.168.2.58/ aarch64-mx8m/
deb http://192.168.2.58/ maaxboard_ddr4_2g_sdcard/
deb http://192.168.2.58/ all/
```

#### apt-get update

```bash
$ sudo rm -rf /var/lib/apt/lists/*
$ sudo apt-get update
```

#### Install packages

```bash
sudo apt-get install nano
```
