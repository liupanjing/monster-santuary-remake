# 固定同伴与原版对话角色线索

由 `Tools/content_inventory.py` 生成。手工开发状态应修改生成器中的显式实现映射，不能通过名称相似或文件存在授予已实现状态。完整原始参数与每条指令见 [JSON清单](../../Production/Content/content-inventory.json)。

固定同伴独立于50只怪兽。下面的对话发言标签还包含职业名、提示和UI标签，不能把每个标签都算成一个独立NPC；精确角色身份与场景演员绑定在A02中继续核对。

|身份|名称|当前清单状态|认可造型|待恢复|
|---|---|---|---|---|
|companion:babao|八宝|已接入当前章节子集|v2|跟随、剧情和能力分别验收；动作的技术通过不代替视觉认可|
|companion:qibei|七贝|待恢复与接入|None|跟随、剧情和能力分别验收；动作的技术通过不代替视觉认可|

## 全部非数字发言标签

|标签|对话指令数|原脚本文件|示例指令身份|
|---|---|---|---|
|X6员工|12|1026.bin|script:1026.bin@90|
|tishi|2|3032.bin|script:3032.bin@3495|
|“美少女”|1|1032.bin|script:1032.bin@896|
|一等奖|1|1.bin|script:1.bin@0|
|七贝|6|1003.bin, 1020.bin, 1037.bin|script:1003.bin@5645|
|三等奖|1|3.bin|script:3.bin@0|
|丽丝|32|1035.bin, 3031.bin|script:1035.bin@173|
|二等奖|1|2.bin|script:2.bin@0|
|仓库|1|3029.bin|script:3029.bin@0|
|传送石|16|2987.bin, 3992.bin|script:2987.bin@18|
|佣兵任务|1|2995.bin|script:2995.bin@0|
|佣兵所前台|2|3059.bin, 3060.bin|script:3059.bin@0|
|佣兵所职员|5|1040.bin|script:1040.bin@460|
|克莱尔|7|1037.bin|script:1037.bin@5009|
|八宝|185|1003.bin, 1004.bin, 1005.bin, 1006.bin, 1007.bin, 1008.bin, 1009.bin, 1010.bin, 1017.bin, 1018.bin, 1020.bin, 1021.bin, 1022.bin, 1023.bin, 1024.bin, 1025.bin, 1026.bin, 1027.bin, 1028.bin, 1029.bin, 1030.bin, 1031.bin, 1032.bin, 1033.bin, 1034.bin, 1036.bin, 1037.bin, 1039.bin, 1040.bin, 1041.bin, 1042.bin, 1044.bin, 1045.bin, 1047.bin, 11.bin, 12.bin, 2001.bin, 2020.bin, 3001.bin, 3008.bin, 3012.bin, 3017.bin, 3028.bin, 3030.bin, 3032.bin, 3039.bin, 3040.bin, 3041.bin, 3043.bin, 4002.bin, 4003.bin, 8001.bin|script:11.bin@0|
|公奶牛|2|3006.bin|script:3006.bin@238|
|冒险者|8|3016.bin, 3055.bin, 3056.bin, 3057.bin, 3061.bin|script:3016.bin@0|
|农夫|3|3005.bin|script:3005.bin@18|
|力量的考验|11|3039.bin|script:3039.bin@18|
|医生|8|1998.bin, 3036.bin, 3039.bin, 3040.bin, 3041.bin|script:1998.bin@0|
|卫兵|4|1996.bin, 1997.bin, 3061.bin|script:1996.bin@36|
|危险！|1|3997.bin|script:3997.bin@0|
|发明家|9|3026.bin|script:3026.bin@18|
|受伤的路人|25|3027.bin, 3028.bin|script:3027.bin@18|
|四等奖|1|4.bin|script:4.bin@0|
|地图指引者|10|3061.bin|script:3061.bin@804|
|多姆|11|1022.bin|script:1022.bin@761|
|大叔|1|1022.bin|script:1022.bin@1728|
|大赛管理员|26|1032.bin, 3034.bin, 3998.bin|script:1032.bin@1139|
|奥尼克|436|1003.bin, 1004.bin, 1005.bin, 1006.bin, 1007.bin, 1008.bin, 1009.bin, 1010.bin, 1011.bin, 1012.bin, 1013.bin, 1014.bin, 1015.bin, 1016.bin, 1017.bin, 1018.bin, 1020.bin, 1021.bin, 1022.bin, 1023.bin, 1024.bin, 1025.bin, 1026.bin, 1027.bin, 1028.bin, 1029.bin, 1030.bin, 1031.bin, 1032.bin, 1033.bin, 1034.bin, 1035.bin, 1036.bin, 1037.bin, 1038.bin, 1039.bin, 1040.bin, 1041.bin, 1042.bin, 1044.bin, 1045.bin, 1046.bin, 1047.bin, 2001.bin, 2020.bin, 3001.bin, 3002.bin, 3003.bin, 3005.bin, 3006.bin, 3008.bin, 3012.bin, 3014.bin, 3016.bin, 3017.bin, 3021.bin, 3026.bin, 3027.bin, 3028.bin, 3030.bin, 3031.bin, 3032.bin, 3033.bin, 3039.bin, 3040.bin, 3041.bin, 3042.bin, 3043.bin, 3044.bin, 3046.bin, 3063.bin, 3064.bin, 4002.bin, 4003.bin, 8001.bin, 999.bin|script:999.bin@176|
|女人|1|3024.bin|script:3024.bin@0|
|女生|2|3061.bin|script:3061.bin@301|
|客人|3|3013.bin|script:3013.bin@18|
|小依|6|1006.bin, 3002.bin|script:1006.bin@108|
|小发明家|3|3062.bin|script:3062.bin@18|
|小孩|2|3025.bin, 3061.bin|script:3025.bin@0|
|师师|1|3045.bin|script:3045.bin@0|
|怪兽技能商人|3|3046.bin, 3061.bin|script:3046.bin@18|
|怪兽调教师|1|3047.bin|script:3047.bin@0|
|技能商人|7|3061.bin|script:3061.bin@3213|
|探险任务|1|3991.bin|script:3991.bin@0|
|提示|93|1008.bin, 1028.bin, 1043.bin, 1044.bin, 1999.bin, 2001.bin, 2020.bin, 3002.bin, 3006.bin, 3008.bin, 3009.bin, 3030.bin, 3031.bin, 3032.bin, 3999.bin, 8001.bin|script:1008.bin@2314|
|新区域|1|8002.bin|script:8002.bin@598|
|旅馆|3|3078.bin|script:3078.bin@36|
|旅馆老板|1|3030.bin|script:3030.bin@83|
|智慧的考验|12|3041.bin|script:3041.bin@18|
|未开放区域|1|8002.bin|script:8002.bin@18|
|检票员|12|3993.bin, 3994.bin|script:3993.bin@18|
|武器店老板娘|1|3010.bin|script:3010.bin@0|
|武器锻造师|1|3061.bin|script:3061.bin@3028|
|源质炼化师|5|3044.bin|script:3044.bin@307|
|灵兽能力开启|1|2500.bin|script:2500.bin@0|
|特殊商人|4|3061.bin|script:3061.bin@3488|
|猎人|1|3051.bin|script:3051.bin@0|
|瓦莎|6|1046.bin, 3021.bin|script:1046.bin@36|
|皮皮|4|3028.bin|script:3028.bin@101|
|神秘人|4|1009.bin, 3073.bin|script:1009.bin@2200|
|神秘怪兽|3|1003.bin|script:1003.bin@1741|
|神秘技能商人|7|3054.bin, 3065.bin, 3072.bin|script:3054.bin@0|
|秘境守护者|12|1044.bin, 998.bin|script:998.bin@54|
|管理员|5|1008.bin, 3023.bin, 3068.bin, 3069.bin|script:1008.bin@446|
|精灵的宝箱|4|2062.bin|script:2062.bin@18|
|系统|1|2020.bin|script:2020.bin@18|
|系统提示|1|3008.bin|script:3008.bin@758|
|统计员|4|3052.bin, 3053.bin|script:3052.bin@0|
|网络功能提示|1|3040.bin|script:3040.bin@321|
|罗丽|11|3014.bin, 4003.bin|script:3014.bin@170|
|罗伯斯|9|3033.bin|script:3033.bin@18|
|美少女|12|3043.bin|script:3043.bin@18|
|老头|14|3022.bin, 3063.bin|script:3022.bin@18|
|老奶奶|3|1006.bin, 3003.bin|script:1006.bin@313|
|老妇人|9|3042.bin|script:3042.bin@0|
|老板娘|3|3012.bin|script:3012.bin@36|
|考官|51|1034.bin, 3032.bin|script:1034.bin@324|
|耐法瑞|48|1000.bin, 1005.bin, 1006.bin, 1009.bin, 998.bin, 999.bin|script:998.bin@89|
|联网提示|1|3017.bin|script:3017.bin@1306|
|芙蕾尔|2|1027.bin|script:1027.bin@314|
|血爪|7|1045.bin|script:1045.bin@108|
|西尔多|17|3008.bin|script:3008.bin@18|
|西雅|77|1003.bin, 1020.bin, 1037.bin, 1044.bin, 1045.bin|script:1003.bin@2679|
|许愿池精灵|12|1008.bin, 3064.bin|script:1008.bin@2814|
|诺滋|12|1022.bin|script:1022.bin@1079|
|诺滋和多姆|2|1022.bin|script:1022.bin@1509|
|豆猪|1|1022.bin|script:1022.bin@1981|
|路人|3|3061.bin|script:3061.bin@2029|
|路牌|8|3990.bin|script:3990.bin@36|
|软泥怪|1|1022.bin|script:1022.bin@2082|
|速度的考验|14|3040.bin|script:3040.bin@18|
|道具店老板|1|3001.bin|script:3001.bin@18|
|飞艇管理员|14|1025.bin, 1026.bin, 3012.bin, 3017.bin|script:1025.bin@72|
|驯兽师导师|4|3070.bin, 3071.bin|script:3070.bin@36|
|魔兽商人|4|3022.bin|script:3022.bin@191|
|黑衣人|1|3015.bin|script:3015.bin@0|
|Ｘ６员工|10|3014.bin, 3018.bin, 3038.bin, 3048.bin, 3049.bin, 3075.bin, 3076.bin, 3077.bin|script:3014.bin@18|
|Ｘ６宝箱|1|2998.bin|script:2998.bin@0|
