package net.xuele.member;

import java.util.List;
import java.util.Set;

/**
 * Created by wuxh on 15/9/1.
 */
public class HelloMain {

    final static String str = "* h1. 职务操作\n" +
            "----\n" +
            "** h2. 新增职务\n" +
            "\n" +
            "*** 接口路径:[http://www.xueleyun.com/member/position/add]\n" +
            "*** 传入参数:*name*(职务名称)\n" +
            "*** 返回值: {code}{\"status\":\"1\",\"errorMsg\":null,\"wrapper\":{\"positionId\":\"49123ab1-917a-4c1e-998b-a113ca414ef0\",\"name\":\"测试职务\",\"description\":\"自定义职务\",\"totals\":0,\"positionType\":\"2\",\"schoolId\":\"fjsjsjss\"}}{code}\n" +
            "*** 调用示例:[http://www.xueleyun.com/member/position/add?name=test&schoolId=49123ab1-917a-4c1e-998b-a113ca414ef0]\n" +
            "** h2. 删除职务\n" +
            "\n" +
            "*** 接口路径:[http://www.xueleyun.com/member/position/delete|http://www.xueleyun.com/member/position/delete]\n" +
            "*** 传入参数:*positionId*(职务ID)\n" +
            "*** 返回值: {code}{\"status\":\"1\",\"errorMsg\":null,\"wrapper\":null}{code}\n" +
            "*** 示例:[http://www.xueleyun.com/member/position/delete?positionId=1]\n" +
            "** h2. 修改职务\n" +
            "\n" +
            "*** 接口路径:[http://www.xueleyun.com/member/position/update]\n" +
            "*** 传入参数:*name*(职务名称),*positionId*(职务ID)\n" +
            "*** 返回值:\n" +
            "{code}{\"status\":\"1\",\"errorMsg\":null,\"wrapper\": {\"positionId\":\"49123ab1-917a-4c1e-998b-a113ca414ef0\",\"name\":\"testupdate\",\"description\":\"自定义职务\",\"totals\":0,\"positionType\":\"2\",\"schoolId\":\"fjsjsjss\"}}{code}\n" +
            "*** 示例:[http://www.xueleyun.com/member/position/update?positionId=1&name=testupdate]\n" +
            "\n" +
            "\n" +
            "* h1. 班级操作\n" +
            "----\n" +
            "** h2. 新增班级\n" +
            "\n" +
            "*** 接口路径:[http://www.xueleyun.com/member/class/insertClass]\n" +
            "*** 传入参数:*nick*(班级别名),*year*(年级的入学年份)\n" +
            "*** 返回值: {code}{\"status\":\"1\",\"errorMsg\":null,\"wrapper\":{\"classId\":\"c7e16476e9f746d4b80a73aae01545ef\",\"aliasName\":\"班级别名\",\"name\":\"(8)班\",\"chargeId\":null,\"chargeName\":null,\"studentNumber\":0}}{code}\n" +
            "*** 调用示例:[http://www.xueleyun.com/member/class/insertClass?nick=班级别名&year=2014|http://www.xueleyun.com/member/class/insertClass?nick=班级别名&g=2014]\n" +
            "\n" +
            "** h2. 删除班级\n" +
            "\n" +
            "*** 接口路径:[http://www.xueleyun.com/member/class/deleteClass]\n" +
            "*** 传入参数:*id*(班级ID)\n" +
            "*** 返回值: {code}{\"status\":\"1\",\"errorMsg\":null,\"wrapper\":null}{code}\n" +
            "*** 调用示例:[http://www.xueleyun.com/member/class/deleteClass?id=378b403ce8ee4fd9bb70163bd10028a4]\n" +
            "\n" +
            "** h2. 重命名\n" +
            "\n" +
            "*** 接口路径:[http://www.xueleyun.com/member/class/saveClass]\n" +
            "*** 传入参数:*id*(班级ID),*nick*(班级别名)\n" +
            "*** 返回值: {code}{\"status\":\"1\",\"errorMsg\":null,\"wrapper\":{\"classId\":\"1b9d321529fc4c60a48bc3f4e7c8e287\",\"aliasName\":\"nice\",\"name\":null,\"chargeId\":null,\"chargeName\":null,\"studentNumber\":0}}{code}\n" +
            "*** 调用示例:[http://www.xueleyun.com/member/class/saveClass?id=f56d3791fd4a49f78271de62294be920&nick=nice]\n" +
            "\n" +
            "** h2. 添加班主任\n" +
            "\n" +
            "*** 接口路径:[http://www.xueleyun.com/member/class/setChief]\n" +
            "*** 传入参数:*chiefId*(班级任ID),*classId*(班级Id)\n" +
            "*** 返回值: {code}{\"status\":\"1\",\"errorMsg\":null,\"wrapper\":{\"userId\":\"id\",\"realName\":\"李四\",\"icon\":\"http://192.168.1.39/member/picture/picture\"}}{code}\n" +
            "*** 调用示例:[http://www.xueleyun.com/member/class/setChief?chiefId=1&classId=1]\n" +
            "\n" +
            "** h2. 学生换班或者学生加入某个班级\n" +
            "\n" +
            "*** 接口路径:[http://www.xueleyun.com/member/class/addStudents]\n" +
            "*** 传入参数:*userIds*(学生id字符串,用逗号分隔),*classId*(班级Id)\n" +
            "*** 返回值: {code}{\"status\":\"1\",\"errorMsg\":null,\"wrapper\":{\"userIds\":\"1,2,x,4\",\"classId\":\"classId\"}}{code}\n" +
            "*** 调用示例:[http://member.app.xuele.net/member/class/addStudents?userIds=1,2,x,4&classId=classId]\n" +
            "\n" +
            "** h2. 根据年级ID获取班级列表（含班级总人数）\n" +
            "\n" +
            "*** 接口路径:[http://www.xueleyun.com/member/class/selectClass]\n" +
            "*** 传入参数:*year*(年级)\n" +
            "*** 返回值: {code}{\"status\":\"1\",\"errorMsg\":null,\"wrapper\":[{\"classId\":\"1\",\"aliasName\":\"1\",\"name\":\"1年级1班\",\"chargeId\":\"1\",\"chargeName\":\"1\",\"studentNumber\":2},{\"classId\":\"2\",\"aliasName\":\"2\",\"name\":\"1年级2班\",\"chargeId\":null,\"chargeName\":null,\"studentNumber\":6}]}{code}\n" +
            "*** 调用示例:[http://member.app.xuele.net/member/class/selectClass?year=2014]\n" +
            "\n" +
            "** h2. 根据姓名搜索本校所有学生\n" +
            "\n" +
            "*** 接口路径:[http://www.xueleyun.com/member/class/selectStudentByRealName]\n" +
            "*** 传入参数:*realName*(姓名)\n" +
            "*** 返回值: {code}{\"status\":\"1\",\"errorMsg\":null,\"wrapper\":[{\"userId\":\"1\",\"realName\":\"1\",\"icon\":\"1\",\"subjectId\":null,\"subjectName\":null},{\"userId\":\"2\",\"realName\":\"1\",\"icon\":\"22\",\"subjectId\":null,\"subjectName\":null}]}{code}\n" +
            "*** 调用示例:[http://member.app.xuele.net/member/class/selectStudentByRealName?realName=1]\n" +
            "\n" +
            "\n" +
            "\n" +
            "** h2. 根据班级ID，获取所有学生列表 （无班级）\n" +
            "\n" +
            "*** 接口路径:[http://www.xueleyun.com/member/class/selectStudentsByClassId]\n" +
            "*** 传入参数:*classId*(班级Id,为空则查询无班级学生)\n" +
            "*** 返回值: {code}{\"status\":\"1\",\"errorMsg\":null,\"wrapper\":[{\"userId\":\"1\",\"realName\":\"1\",\"icon\":\"1\",\"subjectId\":null,\"subjectName\":null},{\"userId\":\"2\",\"realName\":\"1\",\"icon\":\"22\",\"subjectId\":null,\"subjectName\":null}]}{code}\n" +
            "*** 调用示例:[http://member.app.xuele.net/member/class/selectStudentsByClassId?classId=1]\n" +
            "\n" +
            "** h2. 根据用户所在学校，获取科目列表\n" +
            "\n" +
            "*** 接口路径:[http://www.xueleyun.com/member/student/selectSubject]\n" +
            "*** 传入参数:\n" +
            "*** 返回值: {code}{\"status\":\"1\",\"errorMsg\":null,\"wrapper\":[{\"summaryCode\":\"010\",\"summaryName\":\"语文\",\"summaryMark\":\"Languages\"},{\"summaryCode\":\"020\",\"summaryName\":\"数学\",\"summaryMark\":\"Mathematics\"},{\"summaryCode\":\"030\",\"summaryName\":\"英语\",\"summaryMark\":\"English\"},{\"summaryCode\":\"040\",\"summaryName\":\"科学\",\"summaryMark\":\"Science\"},{\"summaryCode\":\"050\",\"summaryName\":\"物理\",\"summaryMark\":\"Physics\"},{\"summaryCode\":\"060\",\"summaryName\":\"化学\",\"summaryMark\":\"Chemistry\"},{\"summaryCode\":\"070\",\"summaryName\":\"生物\",\"summaryMark\":\"Biology\"},{\"summaryCode\":\"080\",\"summaryName\":\"地理\",\"summaryMark\":\"Geography\"},{\"summaryCode\":\"090\",\"summaryName\":\"政治\",\"summaryMark\":\"Politics\"},{\"summaryCode\":\"100\",\"summaryName\":\"历史\",\"summaryMark\":\"History\"},{\"summaryCode\":\"110\",\"summaryName\":\"体育\",\"summaryMark\":\"PE\"},{\"summaryCode\":\"120\",\"summaryName\":\"艺术\",\"summaryMark\":\"Art\"},{\"summaryCode\":\"130\",\"summaryName\":\"音乐\",\"summaryMark\":\"Music\"},{\"summaryCode\":\"140\",\"summaryName\":\"手工课\",\"summaryMark\":\"HandWork\"},{\"summaryCode\":\"150\",\"summaryName\":\"信息技术\",\"summaryMark\":\"IT\"},{\"summaryCode\":\"160\",\"summaryName\":\"思想品德\",\"summaryMark\":\"MoralEducation\"},{\"summaryCode\":\"170\",\"summaryName\":\"艺术\",\"summaryMark\":\"Yishu\"}]}{code}\n" +
            "*** 调用示例:[http://www.xueleyun.com/member/student/selectSubject]\n" +
            "\n" +
            "** h2. 根据科目或老师名字获取老师\n" +
            "\n" +
            "*** 接口路径:[http://www.xueleyun.com/member/class/selectTeaByNameOrSubId?realName=111&subjectId=010]\n" +
            "*** 传入参数:*realName*(姓名),*subjectId*(科目id),\n" +
            "*** 返回值: {code}{\"status\":\"1\",\"errorMsg\":null,\"wrapper\":[{\"userId\":\"56077\",\"realName\":\"teacher\",\"icon\":\"00000000000000000000000000000000\",\"subjectId\":\"010\",\"subjectName\":\"语文\",\"charge\":0,\"positionId\":\"PRINCIPAL\",\"positionName\":\"校长\",\"isManager\":1},{\"userId\":\"11548826\",\"realName\":\"吴玲\",\"icon\":\"00000000000000000000000000000000\",\"subjectId\":\"010\",\"subjectName\":\"语文\",\"charge\":0,\"positionId\":\"3a2ac45766d0401aadb070524eb39cf5\",\"positionName\":\"123456\",\"isManager\":1}]}{code}\n" +
            "*** 调用示例:[http://www.xueleyun.com/member/class/selectTeaByNameOrSubId?realName=111&subjectId=010]\n" +
            "\n" +
            "* h1. 学生管理\n" +
            "----\n" +
            "** h2. 新增学生\n" +
            "\n" +
            "*** 接口路径:[http://www.xueleyun.com/member/student/add]\n" +
            "*** 传入参数:*name*(学生名字),*classId*(班级ID)\n" +
            "*** 返回值: {code}{\"status\":\"1\",\"errorMsg\":null,\"wrapper\":{\"userId\":\"1001010\",\"realName\":\"张三\",\"classAliasName\":\"冲刺班\",\"classId\":\"1001\",\"icon\":\"http://192.168.1.39/member/file/getPicture?id=iconId&max=12\",\"lastLoginDate\":null}}{code}\n" +
            "*** 调用示例:[http://www.xueleyun.com/member/student/add?name=张三&classId=1001010]\n" +
            "\n" +
            "** h2. 修改学生\n" +
            "\n" +
            "*** 接口路径:[http://www.xueleyun.com/member/student/update]\n" +
            "*** 传入参数:*userId*(用户ID),*name*(学生名字),*classId*(班级ID)\n" +
            "*** 返回值: {code}{\"status\":\"1\",\"errorMsg\":null,\"wrapper\":{\"userId\":\"1001010\",\"realName\":\"张三\",\"classAliasName\":\"冲刺班\",\"classId\":\"1001\",\"icon\":\"http://192.168.1.39/member/file/getPicture?id=iconId&max=12\",\"lastLoginDate\":null}}{code}\n" +
            "*** 调用示例:[http://www.xueleyun.com/member/student/add?userId=1001010&name=张三&classId=1001010]\n" +
            "\n" +
            "\n" +
            "* h1. 用户操作\n" +
            "----\n" +
            "** h2. 学生、老师离校(批量)\n" +
            "\n" +
            "*** 接口路径:[http://www.xueleyun.com/member/user/leaveSchool]\n" +
            "*** 传入参数:*userIds*(用户id列表，逗号分隔)\n" +
            "*** 返回值: {code}{\"status\":\"1\",\"errorMsg\":null,\"wrapper\":null}{code}\n" +
            "*** 调用示例:[http://www.xueleyun.com/member/user/leaveSchool?userIds=10011002,10011003,10011004,10011005,10011006]\n" +
            "\n" +
            "** h2. 学生、老师返校(批量)\n" +
            "\n" +
            "*** 接口路径:[http://www.xueleyun.com/member/user/backToSchool]\n" +
            "*** 传入参数:*userIds*(用户id列表，逗号分隔)\n" +
            "*** 返回值: {code}{\"status\":\"1\",\"errorMsg\":null,\"wrapper\":null}{code}\n" +
            "*** 调用示例:[http://www.xueleyun.com/member/user/backToSchool?userIds=10011002,10011003,10011004,10011005,10011006]\n" +
            "\n" +
            "** h2. 重置密码\n" +
            "\n" +
            "*** 接口路径:[http://www.xueleyun.com/member/user/resetPassword]\n" +
            "*** 传入参数:*userId*(用户id)\n" +
            "*** 返回值: {code}{\"status\":\"1\",\"errorMsg\":null,\"wrapper\":null}{code}\n" +
            "*** 调用示例:[http://www.xueleyun.com/member/user/resetPassword?userIds=10011002]\n" +
            "\n" +
            "** h2. 获取用户数据\n" +
            "\n" +
            "*** 接口路径:[http://www.xueleyun.com/member/user/getInfo]\n" +
            "*** 传入参数:*userId*(用户id)\n" +
            "*** 返回值: {code}{\"status\":\"1\",\"errorMsg\":null,\"wrapper\":{\"user\":{\"userId\":\"student\",\"realName\":\"学生\",\"sex\":0,\"birthday\":622656000000,\"icon\":\"1\",\"qq\":\"331077064\",\"rType\":0,\"addTime\":1435248000000,\"regType\":0,\"regIp\":\"注册ip\",\"status\":1,\"tel\":\"电话号码\",\"mobile\":\"手机\",\"email\":\"邮箱\",\"signature\":\"个人签名\",\"cover\":\"封面\",\"updateTime\":1435316654000,\"idcard\":\"身份证\",\"identityId\":\"student\",\"identityDescription\":\"老师\",\"accountId\":null},\"profile\":{\"userId\":\"student\",\"classId\":\"班级id\",\"className\":\"班级名字\",\"studentNumber\":\"学籍号\",\"familyName\":\"家庭名称\",\"familyCover\":\"家庭封面\",\"schoolId\":\"学校id\",\"schoolName\":\"学校名字\",\"year\":2014,\"gradeName\":\"一年级\"}}}\n" +
            "{\"status\":\"1\",\"errorMsg\":null,\"wrapper\":{\"user\":{\"userId\":\"teacher\",\"realName\":\"老师\",\"sex\":0,\"birthday\":622656000000,\"icon\":\"1\",\"qq\":\"331077064\",\"rType\":0,\"addTime\":1435248000000,\"regType\":0,\"regIp\":\"注册ip\",\"status\":1,\"tel\":\"电话号码\",\"mobile\":\"手机\",\"email\":\"邮箱\",\"signature\":\"个人签名\",\"cover\":\"封面\",\"updateTime\":1435316654000,\"idcard\":\"身份证\",\"identityId\":\"teacher\",\"identityDescription\":\"老师\",\"accountId\":null},\"profile\":{\"userId\":\"teacher\",\"schoolId\":\"学校id\",\"schoolName\":\"\",\"bookId\":null,\"positionId\":null,\"positionName\":null,\"isManager\":null}}}\n" +
            "{\"status\":\"1\",\"errorMsg\":null,\"wrapper\":{\"user\":{\"userId\":\"parents\",\"realName\":\"家长\",\"sex\":0,\"birthday\":622656000000,\"icon\":\"1\",\"qq\":\"331077064\",\"rType\":0,\"addTime\":1435248000000,\"regType\":0,\"regIp\":\"注册ip\",\"status\":1,\"tel\":\"电话号码\",\"mobile\":\"手机\",\"email\":\"邮箱\",\"signature\":\"个人签名\",\"cover\":\"封面\",\"updateTime\":1435316654000,\"idcard\":\"身份证\",\"identityId\":\"parents\",\"identityDescription\":\"老师\",\"accountId\":null},\"profile\":{\"userId\":\"parents\",\"appellation\":\"家长称谓\"}}}\n" +
            "{\"status\":\"1\",\"errorMsg\":null,\"wrapper\":{\"user\":{\"userId\":\"education\",\"realName\":\"教育机构员工\",\"sex\":0,\"birthday\":622656000000,\"icon\":\"1\",\"qq\":\"331077064\",\"rType\":0,\"addTime\":1435248000000,\"regType\":0,\"regIp\":\"注册ip\",\"status\":1,\"tel\":\"电话号码\",\"mobile\":\"手机\",\"email\":\"邮箱\",\"signature\":\"个人签名\",\"cover\":\"封面\",\"updateTime\":1435316654000,\"idcard\":\"身份证\",\"identityId\":\"education\",\"identityDescription\":\"老师\",\"accountId\":null},\"profile\":{\"userId\":\"education\",\"educationalId\":\"所属教育机构ID\",\"educationalName\":\"所属教育机构名称\",\"dutyId\":\"职务id\",\"dutyName\":\"职务名称\"}}}\n" +
            "{\"status\":\"1\",\"errorMsg\":null,\"wrapper\":{\"user\":{\"userId\":\"education_manager\",\"realName\":\"教育机构管理员\",\"sex\":0,\"birthday\":622656000000,\"icon\":\"1\",\"qq\":\"331077064\",\"rType\":0,\"addTime\":1435248000000,\"regType\":0,\"regIp\":\"注册ip\",\"status\":1,\"tel\":\"电话号码\",\"mobile\":\"手机\",\"email\":\"邮箱\",\"signature\":\"个人签名\",\"cover\":\"封面\",\"updateTime\":1435316654000,\"idcard\":\"身份证\",\"identityId\":\"education_manager\",\"identityDescription\":\"老师\",\"accountId\":null},\"profile\":{\"userId\":\"education_manager\",\"educationalId\":\"所属教育机构ID\",\"educationalName\":\"所属教育机构名称\"}}}\n" +
            "{\"status\":\"1\",\"errorMsg\":null,\"wrapper\":{\"user\":{\"userId\":\"school_manager\",\"realName\":\"学校管理员\",\"sex\":0,\"birthday\":622656000000,\"icon\":\"1\",\"qq\":\"331077064\",\"rType\":0,\"addTime\":1435248000000,\"regType\":0,\"regIp\":\"注册ip\",\"status\":1,\"tel\":\"电话号码\",\"mobile\":\"手机\",\"email\":\"邮箱\",\"signature\":\"个人签名\",\"cover\":\"封面\",\"updateTime\":1435316654000,\"idcard\":\"身份证\",\"identityId\":\"school_manager\",\"identityDescription\":\"老师\",\"accountId\":null},\"profile\":{\"userId\":\"school_manager\",\"schoolId\":\"所属学校ID\",\"schoolName\":\"所属学校名称\"}}}\n" +
            "{code}\n" +
            "*** 调用示例:[http://www.xueleyun.com/member/user/selectByUserId?userId=student]\n" +
            "\n" +
            "** h2. 保存头像\n" +
            "\n" +
            "*** 接口路径:[http://www.xueleyun.com/member/user/saveIcon]\n" +
            "*** 传入参数:*key*(userId:key)\n" +
            "*** 返回值: {code}{\"status\":\"1\",\"errorMsg\":null,\"wrapper\":null}{code}\n" +
            "*** 调用示例:[http://www.xueleyun.com/member/user/saveIcon?key=123]\n" +
            "\n" +
            "** h2. 保存完头像重置缓存中的头像\n" +
            "\n" +
            "*** 接口路径:[http://www.xueleyun.com/member/user/resetIcon]\n" +
            "*** 传入参数:\n" +
            "*** 返回值: {code}{\"status\":\"1\",\"errorMsg\":null,\"wrapper\":null}{code}\n" +
            "*** 调用示例:[http://www.xueleyun.com/member/user/resetIcon]\n" +
            "\n" +
            "* h1. 老师管理\n" +
            "----\n" +
            "** h2. 新增老师\n" +
            "\n" +
            "*** 接口路径:[http://www.xueleyun.com/member/teacher/add]\n" +
            "*** 传入参数:*name*(老师名称),*positionId*(职务ID)\n" +
            "*** 返回值: {code}{\"status\":\"1\",\"errorMsg\":null,\"wrapper\":{\"userId\":\"10001000\",\"realName\":\"testName\",\"positionId\":\"1234\",\"positionName\":\"教师\",\"icon\":\"http://member.app.xuele.net/member/file/getPicture?id=icon&max=50\",\"lastLoginTime\":\"2015-06-27 16:33:14\"}}{code}\n" +
            "*** 调用示例:[http://www.xueleyun.com/member/teacher/add?name=testName&positionId=1234]\n" +
            "\n" +
            "** h2. 修改老师\n" +
            "\n" +
            "*** 接口路径:[http://www.xueleyun.com/member/teacher/update]\n" +
            "*** 传入参数:*userId*(用户id),*name*(老师名称),*positionId*(职务ID)\n" +
            "*** 返回值: {code}{\"status\":\"1\",\"errorMsg\":null,\"wrapper\":{\"userId\":\"1234\",\"realName\":\"test\",\"positionId\":\"12345\",\"positionName\":\"教师\",\"icon\":\"http://member.app.xuele.net/member/file/getPicture?id=icon&max=50\",\"lastLoginTime\":\"2015-06-27 16:34:46\"}} {code}\n" +
            "*** 调用示例:[http://www.xueleyun.com/member/teacher/update?userId=1234&name=test&positionId=12345]\n" +
            "\n" +
            "** h2. 设置老师为管理层\n" +
            "\n" +
            "*** 接口路径:[http://www.xueleyun.com/member/teacher/setManagers]\n" +
            "*** 传入参数:*userIds*(用户id一个或多个)\n" +
            "*** 返回值: {code}{\"status\":\"1\",\"errorMsg\":null,\"wrapper\":{\"userIds\":\"1,2,3\"}}{code}\n" +
            "*** 调用示例:[http://www.xueleyun.com/member/teacher/setManagers?userIds=1,2,3]\n" +
            "\n" +
            "** h2. 删除管理层\n" +
            "\n" +
            "*** 接口路径:[http://www.xueleyun.com/member/teacher/deleteManagers]\n" +
            "*** 传入参数:*userIds*(用户id一个或多个)\n" +
            "*** 返回值: {code}{\"status\":\"1\",\"errorMsg\":null,\"wrapper\":{\"userIds\":\"1,2,3\"}}{code}\n" +
            "*** 调用示例:[http://www.xueleyun.com/member/teacher/deleteManagers?userIds=1,2,3]\n" +
            "\n" +
            "** h2. 设置校长\n" +
            "\n" +
            "*** 接口路径:[http://www.xueleyun.com/member/teacher/setPrincipal]\n" +
            "*** 传入参数:*userId*(用户id)\n" +
            "*** 返回值: {code}{\"status\":\"1\",\"errorMsg\":null,\"wrapper\":{\"primaryUserId\":\"原来校长id,为null代表原来没有校长\",\"userId\":\"abcd\"}}{code}\n" +
            "*** 调用示例:[http://www.xueleyun.com/member/teacher/setPrincipal?userId=abcd]\n" +
            "\n" +
            "** h2. 获取当前老师教的班级信息\n" +
            "\n" +
            "*** 接口路径:[http://www.xueleyun.com/member/teacher/queryTeacherClass]\n" +
            "*** 传入参数:\n" +
            "*** 返回值: {code}{\"status\":\"1\",\"errorMsg\":null,\"wrapper\":[{\"classId\":\"f2b2ab8844ca4eb9afdd0c7b71e11dda\",\"className\":\"一年级(1)班\",\"aliasName\":\"001\",\"gradeNum\":1,\"semester\":1,\"year\":2015,\"studentCount\":2,\"chargeId\":null,\"chargeName\":null,\"mImage\":\"386f34ab25b0ce0e5ba780cb5bd53d72\",\"gradeName\":null,\"schoolId\":null,\"schoolName\":null}]}{code}\n" +
            "*** 调用示例:[http://www.xueleyun.com/member/teacher/queryTeacherClass]\n" +
            "\n" +
            "** h2. 删除老师授课班级\n" +
            "\n" +
            "*** 接口路径:[http://www.xueleyun.com/member/teacher/delTeacherClass]\n" +
            "*** 传入参数:*classId*(班级Id)\n" +
            "*** 返回值: {code}{\"status\":\"1\",\"errorMsg\":null,\"wrapper\":null}{code}\n" +
            "*** 调用示例:[http://www.xueleyun.com/member/teacher/delTeacherClass?classId=123]\n" +
            "\n" +
            "* h1. 学校\n" +
            "----\n" +
            "** h2. 获取本校所有年级\n" +
            "\n" +
            "*** 接口路径:[http://www.xueleyun.com/member/school/getSchoolGrades]\n" +
            "*** 传入参数:\n" +
            "*** 返回值: {code}{\"status\":\"1\",\"errorMsg\":null,\"wrapper\":[{\"id\":2014,\"level\":1,\"name\":\"一年级\"},{\"id\":2013,\"level\":2,\"name\":\"二年级\"},{\"id\":2012,\"level\":3,\"name\":\"三年级\"},{\"id\":2011,\"level\":4,\"name\":\"四年级\"},{\"id\":2010,\"level\":5,\"name\":\"五年级\"}]}{code}\n" +
            "*** 调用示例:[http://www.xueleyun.com/member/school/getSchoolGrades]\n" +
            "\n" +
            "** h2. 根据年级的入学年份获取下面所有班级\n" +
            "\n" +
            "*** 接口路径:[http://www.xueleyun.com/member/school/getGradeClasses]\n" +
            "*** 传入参数:*year*(年级对应的入学年份)\n" +
            "*** 返回值: {code}{\"status\":\"1\",\"errorMsg\":null,\"wrapper\":[{\"classId\":\"6b707ccee8d6472984869925c8044841\",\"aliasName\":\"先进班0000000000000\",\"name\":\"(1)班\",\"chargeId\":\"143634086899\",\"chargeName\":\"李四\",\"studentNumber\":0,\"year\":2014},{\"classId\":\"414b5c46f8ce407bb6f5c86def0e128a\",\"aliasName\":\"普通班\",\"name\":\"(2)班\",\"chargeId\":\"143634084531\",\"chargeName\":\"张三\",\"studentNumber\":1,\"year\":2014},{\"classId\":\"20bfd6ba07e74199818cb31c3a32b860\",\"aliasName\":\"12123\",\"name\":\"(3)班\",\"chargeId\":null,\"chargeName\":null,\"studentNumber\":0,\"year\":2014},{\"classId\":\"1a14deaf70ae43ed87e7a35fbb5f5ac5\",\"aliasName\":\"12345678901234567890\",\"name\":\"(4)班\",\"chargeId\":null,\"chargeName\":null,\"studentNumber\":0,\"year\":2014}]}{code}\n" +
            "*** 调用示例:[http://www.xueleyun.com/member/school/getGradeClasses?year=2014]\n" +
            "\n" +
            "** h2. 根据地区获取该地区下的学校给前端页面,如果传入areaId,以传入的为准,没传以用户所在地区的areaId为准\n" +
            "\n" +
            "*** 接口路径:[http://www.xueleyun.com/member/school/querySchoolByName]\n" +
            "*** 传入参数:*schoolName*(学校名字,可以不传)，*areaId*(地区id)\n" +
            "*** 返回值: {code}{\"status\":\"1\",\"errorMsg\":null,\"wrapper\":[{\"id\":\"ScjoolIDQATestsm10\",\"name\":\"杭州下城希望小学\",\"englishName\":null,\"sType\":null,\"address\":null,\"tel\":null,\"motto\":null,\"web\":null,\"lengthOfSchool\":null,\"badge\":null,\"cover\":null,\"managerName\":null,\"manager\":null,\"schoolTime\":null,\"addTime\":null,\"area\":\"3301\",\"areaName\":null,\"status\":1,\"about\":null},{\"id\":\"ScjoolIDQATestsm100\",\"name\":\"杭州下城希望小学\",\"englishName\":null,\"sType\":null,\"address\":null,\"tel\":null,\"motto\":null,\"web\":null,\"lengthOfSchool\":null,\"badge\":null,\"cover\":null,\"managerName\":null,\"manager\":null,\"schoolTime\":null,\"addTime\":null,\"area\":\"3301\",\"areaName\":null,\"status\":1,\"about\":null},{\"id\":\"ScjoolIDQATestsm11\",\"name\":\"杭州下城希望小学\",\"englishName\":null,\"sType\":null,\"address\":null,\"tel\":null,\"motto\":null,\"web\":null,\"lengthOfSchool\":null,\"badge\":null,\"cover\":null,\"managerName\":null,\"manager\":null,\"schoolTime\":null,\"addTime\":null,\"area\":\"3301\",\"areaName\":null,\"status\":1,\"about\":null},{\"id\":\"ScjoolIDQATestsm60\",\"name\":\"杭州上城区希望小学\",\"englishName\":null,\"sType\":null,\"address\":null,\"tel\":null,\"motto\":null,\"web\":null,\"lengthOfSchool\":null,\"badge\":null,\"cover\":null,\"managerName\":null,\"manager\":null,\"schoolTime\":null,\"addTime\":null,\"area\":\"3301\",\"areaName\":null,\"status\":1,\"about\":null},{\"id\":\"ScjoolIDQATestsm61\",\"name\":\"杭州上城区希望小学\",\"englishName\":null,\"sType\":null,\"address\":null,\"tel\":null,\"motto\":null,\"web\":null,\"lengthOfSchool\":null,\"badge\":null,\"cover\":null,\"managerName\":null,\"manager\":null,\"schoolTime\":null,\"addTime\":null,\"area\":\"3301\",\"areaName\":null,\"status\":1,\"about\":null},{\"id\":\"ScjoolIDQATestxiaoxue\",\"name\":\"杭州下城希望小学\",\"englishName\":null,\"sType\":null,\"address\":null,\"tel\":null,\"motto\":null,\"web\":null,\"lengthOfSchool\":null,\"badge\":null,\"cover\":null,\"managerName\":null,\"manager\":null,\"schoolTime\":null,\"addTime\":null,\"area\":\"3301\",\"areaName\":null,\"status\":1,\"about\":null}]}{code}\n" +
            "*** 调用示例:[http://www.xueleyun.com/member/school/querySchoolByName?schoolName=希望]\n" +
            "\n" +
            "* h1. 学校管理员初始化\n" +
            "----\n" +
            "** h2. 根据年级和科目获取云教学的课本\n" +
            "\n" +
            "*** 接口路径:[http://www.xueleyun.com/member/schoolManagerInit/selectBookByGradeCode]\n" +
            "*** 传入参数:*gradeCode*(年级数据字典code),*subjectId*(科目id)\n" +
            "*** 返回值: {code}{\"status\":\"1\",\"errorMsg\":null,\"wrapper\":[{\"bookId\":\"030001001016031\",\"bookName\":\"英语冀教一起标准版一年级上\",\"subjectId\":\"030\",\"subjectName\":\"英语\",\"grade\":1,\"semester\":1,\"semesterDescribe\":\"上\",\"editionId\":\"016031\"},{\"bookId\":\"030001001016100\",\"bookName\":\"英语冀教一起2012新版一年级上\",\"subjectId\":\"030\",\"subjectName\":\"英语\",\"grade\":1,\"semester\":1,\"semesterDescribe\":\"上\",\"editionId\":\"016100\"},{\"bookId\":\"030001001019016\",\"bookName\":\"英语北师版标准版一年级上\",\"subjectId\":\"030\",\"subjectName\":\"英语\",\"grade\":1,\"semester\":1,\"semesterDescribe\":\"上\",\"editionId\":\"019016\"},{\"bookId\":\"030001001019100\",\"bookName\":\"英语北师版2012新版一年级上\",\"subjectId\":\"030\",\"subjectName\":\"英语\",\"grade\":1,\"semester\":1,\"semesterDescribe\":\"上\",\"editionId\":\"019100\"},{\"bookId\":\"030001001023044\",\"bookName\":\"英语新标准新版一年级上\",\"subjectId\":\"030\",\"subjectName\":\"英语\",\"grade\":1,\"semester\":1,\"semesterDescribe\":\"上\",\"editionId\":\"023044\"},{\"bookId\":\"030001001067100\",\"bookName\":\"英语新起点新版一年级上\",\"subjectId\":\"030\",\"subjectName\":\"英语\",\"grade\":1,\"semester\":1,\"semesterDescribe\":\"上\",\"editionId\":\"067100\"},{\"bookId\":\"030001001099100\",\"bookName\":\"英语广东版Kid’s English标准版一年级上\",\"subjectId\":\"030\",\"subjectName\":\"英语\",\"grade\":1,\"semester\":1,\"semesterDescribe\":\"上\",\"editionId\":\"099100\"},{\"bookId\":\"030001001116100\",\"bookName\":\"英语剑桥国际少儿英语标准版一年级上\",\"subjectId\":\"030\",\"subjectName\":\"英语\",\"grade\":1,\"semester\":1,\"semesterDescribe\":\"上\",\"editionId\":\"116100\"},{\"bookId\":\"030001001118100\",\"bookName\":\"英语上海牛津版标准版一年级上\",\"subjectId\":\"030\",\"subjectName\":\"英语\",\"grade\":1,\"semester\":1,\"semesterDescribe\":\"上\",\"editionId\":\"118100\"},{\"bookId\":\"030001002016031\",\"bookName\":\"英语冀教一起标准版一年级下\",\"subjectId\":\"030\",\"subjectName\":\"英语\",\"grade\":1,\"semester\":2,\"semesterDescribe\":\"下\",\"editionId\":\"016031\"},{\"bookId\":\"030001002016100\",\"bookName\":\"英语冀教一起2012新版一年级下\",\"subjectId\":\"030\",\"subjectName\":\"英语\",\"grade\":1,\"semester\":2,\"semesterDescribe\":\"下\",\"editionId\":\"016100\"},{\"bookId\":\"030001002019100\",\"bookName\":\"英语北师版2012新版一年级下\",\"subjectId\":\"030\",\"subjectName\":\"英语\",\"grade\":1,\"semester\":2,\"semesterDescribe\":\"下\",\"editionId\":\"019100\"},{\"bookId\":\"030001002023044\",\"bookName\":\"英语新标准新版一年级下\",\"subjectId\":\"030\",\"subjectName\":\"英语\",\"grade\":1,\"semester\":2,\"semesterDescribe\":\"下\",\"editionId\":\"023044\"},{\"bookId\":\"030001002067100\",\"bookName\":\"英语新起点新版一年级下\",\"subjectId\":\"030\",\"subjectName\":\"英语\",\"grade\":1,\"semester\":2,\"semesterDescribe\":\"下\",\"editionId\":\"067100\"},{\"bookId\":\"030001002099100\",\"bookName\":\"英语广东版Kid’s English标准版一年级下\",\"subjectId\":\"030\",\"subjectName\":\"英语\",\"grade\":1,\"semester\":2,\"semesterDescribe\":\"下\",\"editionId\":\"099100\"},{\"bookId\":\"030001002116100\",\"bookName\":\"英语剑桥国际少儿英语标准版一年级下\",\"subjectId\":\"030\",\"subjectName\":\"英语\",\"grade\":1,\"semester\":2,\"semesterDescribe\":\"下\",\"editionId\":\"116100\"},{\"bookId\":\"030001002118100\",\"bookName\":\"英语上海牛津版标准版一年级下\",\"subjectId\":\"030\",\"subjectName\":\"英语\",\"grade\":1,\"semester\":2,\"semesterDescribe\":\"下\",\"editionId\":\"118100\"}]}{code}\n" +
            "*** 调用示例:[http://www.xueleyun.com/member/schoolManagerInit/selectBookByGradeCode?gradeCode=1&subjectId=030]\\]\n" +
            "\n" +
            "** h2. 设置该学校的云教学课本\n" +
            "\n" +
            "*** 接口路径:[http://www.xueleyun.com/member/schoolManagerInit/setBook]\n" +
            "*** 传入参数:*bookId*(课本Id)\n" +
            "*** 返回值: {code}{\"status\":\"1\",\"errorMsg\":null,\"wrapper\":null}{code}\n" +
            "*** 调用示例:[http://www.xueleyun.com/member/schoolManagerInit/setBook?bookId=123456789]\\]\n" +
            "\n" +
            "** h2. 删除该学校的云教学课本\n" +
            "\n" +
            "*** 接口路径:[http://www.xueleyun.com/member/schoolManagerInit/deleteBook]\n" +
            "*** 传入参数:*bookId*(课本Id)\n" +
            "*** 返回值: {code}{\"status\":\"1\",\"errorMsg\":null,\"wrapper\":null}{code}\n" +
            "*** 调用示例:[http://www.xueleyun.com/member/schoolManagerInit/deleteBook?bookId=123456789]\\]\n" +
            "\n" +
            "** h2. 更换该学校的云教学课本\n" +
            "\n" +
            "*** 接口路径:[http://www.xueleyun.com/member/schoolManagerInit/updateBook]\n" +
            "*** 传入参数:*oldBookId*(要删的课本Id),*newBookId*(要增的课本Id)\n" +
            "*** 返回值: {code}{\"status\":\"1\",\"errorMsg\":null,\"wrapper\":null}{code}\n" +
            "*** 调用示例:[http://www.xueleyun.com/member/schoolManagerInit/updateBook?oldBookId=123&newBookId=321]\\]\n" +
            "\n" +
            "\n" +
            "\n" +
            "** h2. 获取所有科目字典\n" +
            "\n" +
            "*** 接口路径:[http://www.xueleyun.com/member/schoolManagerInit/selectSubject]\n" +
            "*** 传入参数:\n" +
            "*** 返回值: {code}{\"status\":\"1\",\"errorMsg\":null,\"wrapper\":[{\"summaryCode\":\"010\",\"summaryName\":\"语文\",\"summaryMark\":\"Languages\"},{\"summaryCode\":\"020\",\"summaryName\":\"数学\",\"summaryMark\":\"Mathematics\"},{\"summaryCode\":\"030\",\"summaryName\":\"英语\",\"summaryMark\":\"English\"},{\"summaryCode\":\"040\",\"summaryName\":\"科学\",\"summaryMark\":\"Science\"},{\"summaryCode\":\"050\",\"summaryName\":\"物理\",\"summaryMark\":\"Physics\"},{\"summaryCode\":\"060\",\"summaryName\":\"化学\",\"summaryMark\":\"Chemistry\"},{\"summaryCode\":\"070\",\"summaryName\":\"生物\",\"summaryMark\":\"Biology\"},{\"summaryCode\":\"080\",\"summaryName\":\"地理\",\"summaryMark\":\"Geography\"},{\"summaryCode\":\"090\",\"summaryName\":\"政治\",\"summaryMark\":\"Politics\"},{\"summaryCode\":\"100\",\"summaryName\":\"历史\",\"summaryMark\":\"History\"},{\"summaryCode\":\"110\",\"summaryName\":\"体育\",\"summaryMark\":\"PE\"},{\"summaryCode\":\"120\",\"summaryName\":\"艺术\",\"summaryMark\":\"Art\"},{\"summaryCode\":\"130\",\"summaryName\":\"音乐\",\"summaryMark\":\"Music\"},{\"summaryCode\":\"140\",\"summaryName\":\"手工课\",\"summaryMark\":\"HandWork\"},{\"summaryCode\":\"150\",\"summaryName\":\"信息技术\",\"summaryMark\":\"IT\"},{\"summaryCode\":\"160\",\"summaryName\":\"思想品德\",\"summaryMark\":\"MoralEducation\"},{\"summaryCode\":\"170\",\"summaryName\":\"艺术\",\"summaryMark\":\"Yishu\"}]}{code}\n" +
            "*** 调用示例:[http://www.xueleyun.com/member/schoolManagerInit/selectSubject]\n" +
            "\n" +
            "** h2. 根据课本id获取教辅信息\n" +
            "\n" +
            "*** 接口路径:[http://www.xueleyun.com/member/extraBook/queryByBookId]\n" +
            "*** 传入参数:*bookId*(课本Id)\n" +
            "*** 返回值: {code}{\"status\":\"1\",\"errorMsg\":null,\"wrapper\":[{\"extraBookId\":\"020008001018053AAA\",\"extraBookName\":\"数学青岛版六三制新版八年级上 配套练习册\",\"bookId\":\"020008001018053\",\"status\":1,\"createTime\":null,\"createUser\":null,\"updateTime\":null,\"updateUser\":null},{\"extraBookId\":\"020008001018053BBB\",\"extraBookName\":\"数学青岛版六三制新版八年级上 青岛版新课标互动学习\",\"bookId\":\"020008001018053\",\"status\":1,\"createTime\":null,\"createUser\":null,\"updateTime\":null,\"updateUser\":null},{\"extraBookId\":\"020008001018053CCC\",\"extraBookName\":\"数学青岛版六三制新版八年级上 青岛版行知天下\",\"bookId\":\"020008001018053\",\"status\":1,\"createTime\":null,\"createUser\":null,\"updateTime\":null,\"updateUser\":null}]}{code}\n" +
            "*** 调用示例:[http://www.xueleyun.com/member/extraBook/queryByBookId?bookId=020008001018053]\n" +
            "\n" +
            "** h2. 设置学校课本关联表的教辅id,并根据教辅id获取教辅信息\n" +
            "\n" +
            "*** 接口路径:[http://www.xueleyun.com/member/extraBook/setExtraBookId]\n" +
            "*** 传入参数:*bookId*(课本Id),*extraBookId*(教辅Id)\n" +
            "*** 返回值: {code}{\"status\":\"1\",\"errorMsg\":null,\"wrapper\":{\"extraBookId\":\"020007001004034AAA\",\"extraBookName\":\"数学人教版新版七年级上 南方新课堂金牌学案\",\"bookId\":\"020007001004034\",\"status\":1,\"createTime\":null,\"createUser\":null,\"updateTime\":null,\"updateUser\":null}}{code}\n" +
            "*** 调用示例:[http://www.xueleyun.com/member/extraBook/setExtraBookId?bookId=020008001018053&extraBookId=020007001004034AAA]\n" +
            "\n" +
            "\n" +
            "\n" +
            "* h1. 教师初始化\n" +
            "----\n" +
            "** h2. 设置教的课本\n" +
            "\n" +
            "*** 接口路径:[http://www.xueleyun.com/member/teacherInit/setBook]\n" +
            "*** 传入参数:*bookId*(课本Id)\n" +
            "*** 返回值: {code}{\"status\":\"1\",\"errorMsg\":null,\"wrapper\":null}{code}\n" +
            "*** 调用示例:[http://www.xueleyun.com/member/teacherInit/setBook?bookId=0300010010160310]\\]\n" +
            "\n" +
            "** h2. 设置主授课本\n" +
            "\n" +
            "*** 接口路径:[http://www.xueleyun.com/member/teacherInit/setMainBook]\n" +
            "*** 传入参数:*bookId*(课本Id)\n" +
            "*** 返回值: {code}{\"status\":\"1\",\"errorMsg\":null,\"wrapper\":null}{code}\n" +
            "*** 调用示例:[http://www.xueleyun.com/member/teacherInit/setMainBook?bookId=0300010010160310]\\]\n" +
            "\n" +
            "** h2. 删除课本\n" +
            "\n" +
            "*** 接口路径:[http://www.xueleyun.com/member/teacherInit/deleteBook]\n" +
            "*** 传入参数:*bookId*(课本Id)\n" +
            "*** 返回值: {code}{\"status\":\"1\",\"errorMsg\":null,\"wrapper\":null}{code}\n" +
            "*** 调用示例:[http://www.xueleyun.com/member/teacherInit/setMainBook?bookId=0300010010160310]\\]\n" +
            "\n" +
            "** h2. 教师任课班级\n" +
            "\n" +
            "*** 接口路径:[http://www.xueleyun.com/member/teacherInit/setClass]\n" +
            "*** 传入参数:*classId*(班级Id)\n" +
            "*** 返回值: {code}{\"status\":\"1\",\"errorMsg\":null,\"wrapper\":null}{code}\n" +
            "*** 调用示例:[http://www.xueleyun.com/member/teacherInit/setClass?classId=123]\\]\n" +
            "\n" +
            "** h2. 教师更换云教学课本\n" +
            "\n" +
            "*** 接口路径:[http://www.xueleyun.com/member/teacherInit/updateBook]\n" +
            "*** 传入参数:*oldBookId*(要删的课本Id),*newBookId*(要增的课本Id)\n" +
            "*** 返回值: {code}{\"status\":\"1\",\"errorMsg\":null,\"wrapper\":null}{code}\n" +
            "*** 调用示例:[http://www.xueleyun.com/member/schoolManagerInit/updateBook?oldBookId=123&newBookId=321]\\]\n" +
            "\n" +
            "* h1. 安全操作\n" +
            "----\n" +
            "** h2. 绑定邮箱（绑定时，发送邮件接口）\n" +
            "\n" +
            "*** 接口路径:[http://www.xueleyun.com/member/security/bindEmail]\n" +
            "*** 传入参数:*email*(邮箱地址)\n" +
            "*** 返回值: {code}{\"status\":\"1\",\"errorMsg\":null,\"wrapper\":null}{code}\n" +
            "*** 示例:[http://www.xueleyun.com/member/security/bindEmail?email=1718078245@qq.com]\n" +
            "** h2. 绑定手机（已收取验证码，绑定手机）\n" +
            "\n" +
            "*** 接口路径:[http://www.xueleyun.com/member/security/bindMobile]\n" +
            "*** 传入参数:*mobile*(手机号),*checkCode*(手机接收到的验证码)\n" +
            "*** 返回值: {code}{\"status\":\"1\",\"errorMsg\":null,\"wrapper\":null}{code}\n" +
            "*** 示例:[http://www.xueleyun.com/member/security/bindMobile?mobile=12345678956&checkCode=563478]\n" +
            "** h2. 获取绑定手机的验证码（绑定手机时，发送短信）\n" +
            "\n" +
            "*** 接口路径:[http://www.xueleyun.com/member/security/getCheckCodeForBindMobile]\n" +
            "*** 传入参数:*mobile*(接受code的手机号)\n" +
            "*** 返回值: {code}{\"status\":\"1\",\"errorMsg\":null,\"wrapper\":null}{code}\n" +
            "*** 示例:[http://www.xueleyun.com/member/security/getCheckCodeForBindMobile?mobile=13567827863]\n" +
            "\n" +
            "** h2. 获取重置密码的手机验证码（手机重置密码发送短信）\n" +
            "\n" +
            "*** 接口路径:[http://www.xueleyun.com/member/security/anonymous/getCheckCodeForResetPassword]\n" +
            "*** 传入参数:*mobile*(接受code的手机号)\n" +
            "*** 返回值: {code}{\"status\":\"1\",\"errorMsg\":null,\"wrapper\":null}{code}\n" +
            "*** 示例:[http://www.xueleyun.com/member/security/anonymous/getCheckCodeForResetPassword?mobile=13567827863]\n" +
            "\n" +
            "** h2. 获取重置密码的邮箱验证码（邮箱重置密码 之 重新发送邮件）\n" +
            "\n" +
            "*** 接口路径:[http://www.xueleyun.com/member/security/anonymous/getCheckCodeForResetPasswordByEmail]\n" +
            "*** 传入参数:*email*(接受code的手机号，该邮箱需与userId绑定)\n" +
            "*** 返回值: {code}{\"status\":\"1\",\"errorMsg\":null,\"wrapper\":null}{code}\n" +
            "*** 示例:[http://www.xueleyun.com/member/security/anonymous/getCheckCodeForResetPasswordByEmail?email=13567827863@qq.com]\n" +
            "\n" +
            "\n" +
            "** h2. 修改密码（用户通过旧密码修改新密码）\n" +
            "\n" +
            "*** 接口路径:[http://www.xueleyun.com/member/security/changePassword]\n" +
            "*** 传入参数:*oldPassword*(旧密码),*newPassword*(新密码)\n" +
            "*** 返回值: {code}{\"status\":\"1\",\"errorMsg\":null,\"wrapper\":null}{code}\n" +
            "*** 示例:[http://www.xueleyun.com/member/security/changePassword?oldPassword=123456&newPassword=111111]\n" +
            "\n" +
            "\n" +
            "* h1. 邀请家长\n" +
            "----\n" +
            "\n" +
            "\n" +
            "** h2. 获取家长信息（loginType判断是站内邀请还是手机邀请： 0直接邀请，1通过手机验证码注册会员邀请）\n" +
            "\n" +
            "*** 接口路径:[http://www.xueleyun.com/member/family/getParentInfo]\n" +
            "*** 传入参数:*loginId*（家长登录时的学乐号、手机号）,*targetUserId*(学生学乐号）,*memberName*(家长称谓)\n" +
            "*** 返回值: {code}{\"status\":\"1\",\"errorMsg\":null,\"wrapper\":{\"loginId\":\"10086\",\"loginType\":0,\"userId\":\"10086\",\"targetUserId\":\"265143485535\",\"userName\":\"wuxiaohu\",\"icon\":null,\"memeberName\":\"yeye\"}}{code}\n" +
            "*** 示例:[http://www.xueleyun.com/member/family/getParentInfo?loginId=10086&userNmae=wuxiaohu&targetUserId=100861&targetUserName=王五&memberName=爸爸]\n" +
            "\n" +
            "\n" +
            "** h2. 邀请家长（站内邀请,上一步loginType返回0）\n" +
            "\n" +
            "*** 接口路径:[http://www.xueleyun.com/member/family/addFamilyRelation]\n" +
            "*** 传入参数:*userId*(家长学乐号)，*targetUserId*(学生学乐号)，*memberName*(成员称谓)\n" +
            "*** 返回值: {code}{\"status\":\"1\",\"errorMsg\":null,\"wrapper\":{\"id\":\"10086100861\",\"userId\":\"10086\",\"targetUserId\":\"100861\",\"memberId\":\"1\",\"memberName\":\"爸爸\",\"type\":0,\"addTime\":\"2015-08-06\",\"status\":3,\"schoolId\":\"10086\",,\"icon\":\"123456789\"}}{code}\n" +
            "*** 示例:[http://www.xueleyun.com/member/family/addFamilyRelation?userId=10086&userName=王五&targetUserId=100861&targetUserName=五六&memberName=爸爸]\n" +
            "\n" +
            "** h2. 获得邀请家长账户的手机验证码\n" +
            "\n" +
            "*** 接口路径:[http://www.xueleyun.com/member/security/getCheckCodeForInviteUser]\n" +
            "*** 传入参数:*mobile*(被邀请者的手机号，用于接受code),*targetUserId*(学生学乐号），*memberName*(成员称谓)\n" +
            "*** 返回值: {code}{\"status\":\"1\",\"errorMsg\":null,\"wrapper\":null}{code}\n" +
            "*** 示例:[http://www.xueleyun.com/member/security/getCheckCodeForInviteUser?mobile=13567827863&targetUserId=100861&memberName=爸爸]\n" +
            "\n" +
            "\n" +
            "** h2. 邀请家长（手机号邀请）\n" +
            "\n" +
            "*** 接口路径:[http://www.xueleyun.com/member/family/smsAddFamilyRelation]\n" +
            "*** 传入参数:*loginId*(家长登录时的手机号),*checkCode*(手机接收到的验证码),*memberName*(家长称谓),*targetUserId*(学生学乐号)\n" +
            "*** 返回值: {code}{\"status\":\"1\",\"errorMsg\":null,\"wrapper\":null}{code}\n" +
            "*** 示例:[http://www.xueleyun.com/member/family/smsAddFamilyRelation?loginId=13757111229&checkCode=947254&memberName=爸爸&targetUserId=123456]\n" +
            "\n" +
            "\n" +
            "** h2. 获取邀请的用户信息\n" +
            "\n" +
            "*** 接口路径:[http://www.xueleyun.com/member/family/inviteByPlatform]\n" +
            "*** 传入参数:*id*(该家长与该学生对应的关系id)\n" +
            "*** 返回值: {code}{\"status\":\"1\",\"errorMsg\":null,\"wrapper\":{\"id\":\"tjy\",\"userId\":\"dukk1\",\"addTime\":\"2015-08-10\",\"memberName\":\"爷爷\",\"memberRealName\":\"你爸爸\",\"memberIcon\":\"386f34ab25b0ce0e5ba780cb5bd53d72\"，\"status\":1,mobile:\"18368872190\",password:\"123456\"}}{code}\n" +
            "*** 示例:[http://www.xueleyun.com/member/family/inviteByPlatform?id=tjy]\n" +
            "\n" +
            "\n" +
            "** h2. 解除关系\n" +
            "\n" +
            "*** 接口路径:[http://www.xueleyun.com/member/family/releaseRelationship]\n" +
            "*** 传入参数:*id*(该家长与该学生对应的关系id)\n" +
            "*** 返回值: {code}{\"status\":\"1\",\"errorMsg\":null,\"wrapper\":null}{code}\n" +
            "*** 示例:[http://www.xueleyun.com/member/family/releaseRelationship?id=tjy]\n" +
            "\n" +
            "** h2. 站内重新邀请\n" +
            "\n" +
            "*** 接口路径:\\[http://www.xueleyun.com/member/family/reNotify\n" +
            "*** 传入参数:*id*(该家长与该学生对应的关系id)\n" +
            "*** 返回值: {code}{\"status\":\"1\",\"errorMsg\":null,\"wrapper\":null}{code}\n" +
            "*** 示例:[http://www.xueleyun.com/member/family/reNotify?id=tjy]\n" +
            "\n" +
            "** h2. 同意或拒绝\n" +
            "\n" +
            "*** 接口路径:[http://www.xueleyun.com/member/family/doAction]\n" +
            "*** 传入参数:*id*(该家长与该学生对应的关系id),*type*(type=0 拒绝， type=1 同意)\n" +
            "*** 返回值: {code}{\"status\":\"1\",\"errorMsg\":null,\"wrapper\":null}{code}\n" +
            "*** 示例:[http://www.xueleyun.com/member/family/doAction?id=tjy&type=0]\n" +
            "\n" +
            "\n" +
            "* h1. 教务管理\n" +
            "----\n" +
            "\n" +
            "** h2. 获取所在地区的下一级地区\n" +
            "\n" +
            "*** 接口路径:[http://www.xueleyun.com/member/educationManager/queryArea]\n" +
            "*** 传入参数:*areaId*(地区)\n" +
            "*** 返回值: {code}{\"status\":\"1\",\"errorMsg\":null,\"wrapper\":[{\"groupid\":\"110101\",\"groupname\":\"东城区\"},{\"groupid\":\"110102\",\"groupname\":\"西城区\"},{\"groupid\":\"110105\",\"groupname\":\"朝阳区\"},{\"groupid\":\"110106\",\"groupname\":\"丰台区\"},{\"groupid\":\"110107\",\"groupname\":\"石景山区\"},{\"groupid\":\"110108\",\"groupname\":\"海淀区\"},{\"groupid\":\"110109\",\"groupname\":\"门头沟区\"},{\"groupid\":\"110111\",\"groupname\":\"房山区\"},{\"groupid\":\"110112\",\"groupname\":\"通州区\"},{\"groupid\":\"110113\",\"groupname\":\"顺义区\"},{\"groupid\":\"110114\",\"groupname\":\"昌平区\"},{\"groupid\":\"110115\",\"groupname\":\"大兴区\"},{\"groupid\":\"110116\",\"groupname\":\"怀柔区\"},{\"groupid\":\"110117\",\"groupname\":\"平谷区\"}]}{code}\n" +
            "*** 示例:[http://www.xueleyun.com/member/educationManager/queryArea]";

    public static void main(String[] args) {
        UrlCleanUtil urlCleanUtil = new UrlCleanUtil();
        Set<String> urlsInClass = urlCleanUtil.getUrls();
        List<String> urlInDoc = urlCleanUtil.getUrlsFromText("http://www.xueleyun.com/member/", str);
        List<String> strs = urlCleanUtil.urlNotIndoc(urlInDoc, urlsInClass);
        for (String s : strs) {
            System.out.println(s);
        }
    }

}
