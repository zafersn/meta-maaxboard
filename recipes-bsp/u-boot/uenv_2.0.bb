SUMMARY = "U-Boot Env"
SECTION = "app"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/GPL-2.0-only;md5=801f80980d171dd6425610833a22dbe6"

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"
FILES:${PN} = "/boot"

S = "${WORKDIR}"

SRC_URI = " "
SRC_URI = " \
    file://readme-${MACHINE}.txt \
    file://uEnv-${MACHINE}.txt \
"

do_install() {
    install -d ${D}/boot
    install -m 0644 ${S}/uEnv-${MACHINE}.txt ${D}/boot/uEnv.txt
    install -m 0644 ${S}/readme-${MACHINE}.txt ${D}/boot/readme.txt
}

inherit deploy
addtask deploy after do_install

do_deploy () {
    install -m 0644 ${D}/boot/uEnv.txt ${DEPLOYDIR}
    install -m 0644 ${D}/boot/readme.txt ${DEPLOYDIR}
}

COMPATIBLE_MACHINE = "(maaxboardbase)"
PACKAGE_ARCH = "${MACHINE_ARCH}"
