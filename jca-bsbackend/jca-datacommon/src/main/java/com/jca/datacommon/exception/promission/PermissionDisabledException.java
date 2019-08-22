package com.jca.datacommon.exception.promission;

/**权限被禁用
 * @author 
 * @version 1.0
 * @date 
 */
public class PermissionDisabledException extends Exception {
    public PermissionDisabledException() {
        super("该功能权限暂时被禁用！");
    }
}
