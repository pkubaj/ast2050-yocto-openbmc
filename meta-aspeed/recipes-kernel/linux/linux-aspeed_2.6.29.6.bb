# This revision is based on the upstream tag "v2.6.29.6"
# We use the revision in order to avoid having to fetch it from the repo during parse
SRCREV = "201dcfa9d7a12c8e3bfdd4774b5816b2a1a6c29a"

SRC_URI = "git://github.com/pkubaj/ast2050-linux-kernel.git;protocol=https;branch=linux-2.6.29.6"

LINUX_VERSION ?= "2.6.29.6"
LINUX_VERSION_EXTENSION ?= "-aspeed"

PR = "r1"
PV = "${LINUX_VERSION}"

include linux-aspeed.inc

S = "${WORKDIR}/git"

# Install bounds.h for external module install
# The default install script handles this. However, it looks for bounds.h from
# 'include/generated', which doesnot match 2.6.29, where the file is in
# 'include/linux'.
addtask create_generated after do_compile before do_shared_workdir
do_create_generated() {
    install -d ${B}/include/generated
    cp -l ${B}/include/linux/bounds.h ${B}/include/generated/bounds.h
}

# With Fido, ${KERNEL_SRC} is set to ${STAGING_KERENL_DIR}, which is passed
# to kernel module build. So, copy all .h files from the build direcory to
# the ${STAGING_KERNEL_DIR}
addtask copy_to_kernelsrc after do_shared_workdir before do_compile_kernelmodules
do_copy_to_kernelsrc() {
    kerneldir=${STAGING_KERNEL_DIR}/include/linux
    install -d ${kerneldir}
    cp -l ${B}/include/linux/* ${kerneldir}/
}

KERNEL_CC += " --sysroot=${PKG_CONFIG_SYSROOT_DIR}"
