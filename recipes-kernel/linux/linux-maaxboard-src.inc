
KERNEL_SRC:maaxboardbase = "${MAAXBOARD_GIT_HOST_MIRROR}/linux-imx.git;${MAAXBOARD_GIT_PROTOCOL}"
KERNEL_BRANCH:maaxboardbase = "maaxboard_lf-6.1.22-2.0.0"
SRC_URI:maaxboardbase = "${KERNEL_SRC};branch=${KERNEL_BRANCH};${MAAXBOARD_GIT_USER}"

SRCREV:maaxboardbase = "${AUTOREV}"
