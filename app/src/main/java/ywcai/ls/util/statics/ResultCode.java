package ywcai.ls.util.statics;

/**
 * Created by zmy_11 on 2017/4/12.
 */

public class ResultCode {

    public static final String device_status_offline="offline";
    public static final String device_status_online="online";
    public static final String device_status_busy="busy";
    public static final String device_mode_mobile="mobile";
    public static final String device_mode_pc="pc";

    public static final byte byte_head_json = 0xa;
    public static final byte byte_head_byte = 0xb;


    public static final int json_type_req_reg=40101;
    public static final int	json_type_req_add_temp=40102;
    public static final int	json_type_req_reset_code=40103;
    public static final int	json_type_req_edit_name=40104;
    public static final int	json_type_req_remove_device=40105;
    public static final int json_type_req_ctrl_create=40106;
    public static final int json_type_req_ctrl_cut=40107;

    public static final int	json_type_notify_back_reg_fail=40201;
    public static final int json_type_notify_back_reg_ok_with_list=40202;
    public static final int	json_type_notify_back_add_temp_fail=40203;
    public static final int json_type_notify_back_change_abs_fail=40204;
    public static final int json_type_notify_back_change_detail_fail=40205;
    public static final int json_type_notify_back_change_code_fail=40206;

    public static final int json_type_notify_loop_change_abs_ok=40301;
    public static final int json_type_notify_loop_change_detail_ok=40302;
    public static final int json_type_notify_loop_change_code_ok=40303;
    public static final int json_type_notify_loop_add_temp_ok=40304;
    public static final int json_type_notify_loop_turn_on=40305;
    public static final int json_type_notify_loop_turn_off=40306;
    public static final int json_type_notify_loop_turn_busy=40307;
    public static final int json_type_notify_loop_turn_free=40308;
    public static final int json_type_notify_loop_process_up=40309;
    public static final int json_type_notify_loop_process_down=40310;

    public static final int json_type_switch_control_mouse = 42001;
    public static final int json_type_switch_control_key = 42002;
    public static final int json_type_switch_desk_config = 42101;


    //mouse module to pc
    public static final int json_type_req_local_check = 43001;
    public static final int json_type_req_local_close_shadow = 43002;
    public static final int json_type_req_local_open_shadow = 43003;
    public static final int json_type_req_local_open_mouse = 43004;
    public static final int json_type_req_local_close_mouse = 43005;
    public static final int json_type_req_local_repeat_conn = 43006;
    public static final int json_type_req_local_refresh_screen = 43007;

    public static final int json_type_notify_back_check_success = 43101;
    public static final int json_type_notify_back_check_fail = 43102;
    public static final int json_type_notify_back_shadow_open_ok = 43202;
    public static final int json_type_notify_back_shadow_open_fail = 43203;
    public static final int json_type_notify_back_mouse_open_ok = 43301;
    public static final int json_type_notify_back_mouse_open_fail = 43302;
    public static final int json_type_notify_back_shadow_close = 43303;

    public static final int json_type_cmd_mouse_event_move = 44000;
    public static final int json_type_cmd_mouse_event_l_down= 44001;
    public static final int json_type_cmd_mouse_event_l_up= 44002;
    public static final int json_type_cmd_mouse_event_r_down= 44003;
    public static final int json_type_cmd_mouse_event_r_up= 44004;
    public static final int json_type_cmd_mouse_event_page_up= 44005;
    public static final int json_type_cmd_mouse_event_page_down= 44006;
    public static final int json_type_cmd_mouse_event_esc=44007;


}
