<template>
  <div id="buyTicket-Outer">
    <el-card class="box-card">
      <div id="buyTicket-Form">
        <div id="Form-Left">
          <div class="Form-item">
            <el-switch
              v-model="switchValue"
              @change="clearCheckList"
              active-text="换乘"
              inactive-text="直达">
            </el-switch>
          </div>
        </div>
        <div id="Form-Middle">
          <el-row>
            <el-col :span="8">
              <div class="Form-item">
                <span class="Form-text">出发地&nbsp&nbsp&nbsp</span>
                <el-autocomplete :fetch-suggestions="querySearch" placeholder="输入出发地" suffix-icon="el-icon-location-information" v-model="beginPlace"></el-autocomplete>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="Form-item">
                <span class="Form-text">目的地&nbsp&nbsp&nbsp</span>
                <el-autocomplete :fetch-suggestions="querySearch" placeholder="输入目的地" suffix-icon="el-icon-location-information" v-model="endPlace"></el-autocomplete>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="Form-item">
                <span class="Form-text">出发日&nbsp&nbsp&nbsp</span>
                <el-date-picker
                  v-model="beginTime"
                  align="right"
                  type="date"
                  format="yyyy-MM-dd"
                  value-format="yyyy-MM-dd"
                  :picker-options="pickerOptions"
                  placeholder="选择日期">
                </el-date-picker>
              </div>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <div class="Form-item">
                <span class="Form-text">车次类型</span>
                <el-select v-model="trainType" clearable placeholder="请选择" :disabled="switchValue">
                  <el-option
                    v-for="item in trainTypeOptions"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
                  </el-option>
                </el-select>
              </div>
            </el-col>
            <el-col :span="12">
              <div class="Form-item">
                <span class="Form-text">座位类型</span>
                <el-select v-model="seatType" clearable placeholder="请选择" :disabled="switchValue">
                  <el-option
                    v-for="item in seatTypeOptions"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
                  </el-option>
                </el-select>
              </div>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <div class="Form-item">
                <span class="Form-text">换乘选项</span>
                <el-checkbox-group v-model="checkList"  :max="1" :disabled="!switchValue">
                  <el-checkbox label="最快时间"></el-checkbox>
                  <el-checkbox label="最短路程"></el-checkbox>
                  <el-checkbox label="最低价格"></el-checkbox>
                </el-checkbox-group>
              </div>
            </el-col>
          </el-row>
        </div>

        <div id="Form-Right">
          <div class="Form-item">
            <el-button type="primary" icon="el-icon-search" @click="search">搜索</el-button>
          </div>
        </div>
      </div>
    </el-card>

    <el-card class="box-card">
      <el-table
        id="table"
        :data="tableData"
        :span-method="objectSpanMethod"
        height="950"
        border
        empty-text="找不到合适的班车？试试换乘吧"
        :row-class-name="tableRowClassName"
        style="width: 100%">
        <el-table-column type="expand">
          <template slot-scope="props">
            <el-timeline>
              <el-timeline-item
                v-for="(activity, index) in props.row.activities"
                :key="index"
                :icon="activity.icon"
                :type="activity.type"
                :color="activity.color"
                :size="activity.size"
                :timestamp="activity.arrive_date">
                {{activity.sid}}:{{activity.sname}}，距离始发站行程{{activity.mileage}}公里
              </el-timeline-item>
            </el-timeline>
          </template>
        </el-table-column>
        <el-table-column prop="trainID" label="车次" width="80px" header-align="center" align="center"></el-table-column>
        <el-table-column label="出发站——到达站" width="140px" header-align="center">
          <template #default="scope">
            <div class="table-content">
              <div><span style="color: white;background: limegreen">始</span><span style="margin-left: 10px">{{ scope.row.startStation }}</span></div>
              <div><span style="color: white;background: red">终</span><span style="margin-left: 10px">{{ scope.row.endStation }}</span></div>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="出发点——到达点" width="200px" header-align="center">
          <template #default="scope">
            <div class="table-content">
              <div><span style="color: white;background: limegreen">始</span><span style="margin-left: 10px">{{ scope.row.startDate }}</span></div>
              <div><span style="color: white;background: red">终</span><span style="margin-left: 10px">{{ scope.row.endDate }}</span></div>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="商务座" header-align="center">
          <template #default="scope">
            <div class="table-content">
              <div><span>剩余:</span><span style="margin-left: 10px">{{ scope.row.businessSeat.remaining }}</span></div>
              <div><span>价格:</span><span style="margin-left: 10px">{{ scope.row.businessSeat.price }}</span></div>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="firstSeat" label="一等座" header-align="center">
          <template #default="scope">
            <div class="table-content">
              <div><span>剩余:</span><span style="margin-left: 10px">{{ scope.row.firstSeat.remaining }}</span></div>
              <div><span>价格:</span><span style="margin-left: 10px">{{ scope.row.firstSeat.price }}</span></div>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="secondSeat" label="二等座" header-align="center">
          <template #default="scope">
            <div class="table-content">
              <div><span>剩余:</span><span style="margin-left: 10px">{{ scope.row.secondSeat.remaining }}</span></div>
              <div><span>价格:</span><span style="margin-left: 10px">{{ scope.row.secondSeat.price }}</span></div>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="sleeper" label="卧铺" header-align="center">
          <template #default="scope">
            <div class="table-content">
              <div><span>剩余:</span><span style="margin-left: 10px">{{ scope.row.sleeper.remaining }}</span></div>
              <div><span>价格:</span><span style="margin-left: 10px">{{ scope.row.sleeper.price }}</span></div>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="mileage" label="里程" width="70px" header-align="center" align="center"></el-table-column>
<!--        <el-table-column prop="state" label="状态" width="70px" header-align="center" align="center"></el-table-column>-->
        <el-table-column label="操作" header-align="center" align="center" width="260px">
          <template slot="header" slot-scope="scope">
            <div style="display: flex;flex-direction: row;justify-content: space-between;align-items: center">
              <span style="white-space: nowrap;margin-right: 5px">购买</span>
              <el-select :value="ticketType" placeholder="请选择座位类型" @change="changeType">
                <el-option
                  v-for="item in seatTypeOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </div>
          </template>
          <template #default="scope">
            <el-button  size="medium" @click="loadInfo(scope.row,scope.$index)">选择{{typeLabel}}</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card >

    <el-dialog
      title="确认购票信息"
      :visible="dialogVisible2"
      width="80%"
      :before-close="handleClose"
    >
      <div>
        <el-table
          :data="verifyTable"
          stripe
          show-summary
          style="width: 100%">
          <el-table-column
            prop="buyerID"
            width="200px"
            label="购票人">
          </el-table-column>
          <el-table-column
            prop="trainID"
            label="车次">
          </el-table-column>
          <el-table-column
            prop="startStation"
            label="始发站">
          </el-table-column>
          <el-table-column
            prop="endStation"
            label="到站">
          </el-table-column>
          <el-table-column
            prop="startDate"
            width="200px"
            label="出发时间">
          </el-table-column>
          <el-table-column
            prop="endDate"
            width="200px"
            label="到站时间">
          </el-table-column>
          <el-table-column
            prop="seatType"
            label="座位类型">
          </el-table-column>
          <el-table-column
            prop="seatInfo"
            label="座位信息">
          </el-table-column>
          <el-table-column
            prop="price"
            label="价格">
          </el-table-column>
        </el-table>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="scheduled">订 票</el-button>
        <el-button type="primary" @click="buyTicket">购 买</el-button>
        <el-button type="primary" @click="modifyTicket">改 签</el-button>
      </span>
    </el-dialog>

    <el-dialog
      title="提示"
      :visible.sync="dialogVisible3"
      width="30%"
      :before-close="cleanInfo"
      >
      <div style="display: flex;flex-direction: column;align-items: center">
        <h2>请扫码支付{{this.summaryPrice}}元</h2>
        <img src="../assets/QRCode.png">
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="payfor">支 付</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import axios from "axios";
let basePost = axios.create({
  method: 'post',
  headers:{
    "Access-Control-Allow-Origin":"*",
  },
  baseURL:'http://localhost:8090',
})
export default {
  name: "BuyTicket",
  data() {
    return {
      pickerOptions: {
        disabledDate(time) {
          return (time.getTime() - Date.now() > 15 * 24 * 3600 * 1000) || (time.getTime() < Date.now());
        },
        shortcuts: [{
          text: '今天',
          onClick(picker) {
            picker.$emit('pick', new Date());
          }
        }, {
          text: '一周后',
          onClick(picker) {
            const date = new Date();
            date.setTime(date.getTime() + 3600 * 1000 * 24 * 7);
            picker.$emit('pick', date);
          }
        }]
      },
      provinceAndCityData : //provinceAndCityData.province[0]==>省份 json   provinceAndCityData.province[0].city[0]=>省份 json  provinceAndCityData.province[0].city[0].area[0]==>区域 json
        {"province":[
            {"id":1, "code":"110000", "name":"北京市", "city":[
                {"id":2, "code":"110100", "name":"北京市",},
                {"id":19, "code":"110200", "name":'县', }
              ]},
            {"id":22, "code":"120000", "name":"天津市", "city":[
                {"id":23, "code":"120100", "name":"天津市", },
                {"id":39, "code":"120200", "name":"县", }
              ]},
            {"id":43, "code":"130000", "name":"河北省", "city":[
                {"id":44, "code":"130100", "name":"石家庄市",},
                {"id":69, "code":"130200", "name":"唐山市",},
                {"id":85, "code":"130300", "name":"秦皇岛市", },
                {"id":94, "code":"130400", "name":"邯郸市", },
                {"id":115, "code":"130500", "name":"邢台市", },
                {"id":136, "code":"130600", "name":"保定市", },
                {"id":163, "code":"130700", "name":"张家口市", },
                {"id":182, "code":"130800", "name":"承德市", },
                {"id":195, "code":"130900", "name":"沧州市", },
                {"id":213, "code":"131000", "name":"廊坊市", },
                {"id":225, "code":"131100", "name":"衡水市", }
              ]},
            {"id":238, "code":"140000", "name":"山西省", "city":[
                {"id":239, "code":"140100", "name":"太原市", },
                {"id":251, "code":"140200", "name":"大同市", },
                {"id":264, "code":"140300", "name":"阳泉市", },
                {"id":271, "code":"140400", "name":"长治市", },
                {"id":286, "code":"140500", "name":"晋城市", },
                {"id":294, "code":"140600", "name":"朔州市",},
                {"id":302, "code":"140700", "name":"晋中市", },
                {"id":315, "code":"140800", "name":"运城市", },
                {"id":330, "code":"140900", "name":"忻州市", },
                {"id":346, "code":"141000", "name":"临汾市",},
                {"id":365, "code":"141100", "name":"吕梁市",}
              ]},
            {"id":380, "code":"150000", "name":"内蒙古自治区", "city":[
                {"id":381, "code":"150100", "name":"呼和浩特市", },
                {"id":392, "code":"150200", "name":"包头市", },
                {"id":403, "code":"150300", "name":"乌海市", },
                {"id":408, "code":"150400", "name":"赤峰市", },
                {"id":422, "code":"150500", "name":"通辽市", },
                {"id":432, "code":"150600", "name":"鄂尔多斯市", },
                {"id":442, "code":"150700", "name":"呼伦贝尔市", },
                {"id":457, "code":"150800", "name":"巴彦淖尔市", },
                {"id":466, "code":"150900", "name":"乌兰察布市", },
                {"id":479, "code":"152200", "name":"兴安盟",},
                {"id":486, "code":"152500", "name":"锡林郭勒盟", },
                {"id":499, "code":"152900", "name":"阿拉善盟",}
              ]},
            {"id":503, "code":"210000", "name":"辽宁省", "city":[
                {"id":504, "code":"210100", "name":"沈阳市", },
                {"id":519, "code":"210200", "name":"大连市", },
                {"id":531, "code":"210300", "name":"鞍山市", },
                {"id":540, "code":"210400", "name":"抚顺市", },
                {"id":549, "code":"210500", "name":"本溪市",},
                {"id":557, "code":"210600", "name":"丹东市", },
                {"id":565, "code":"210700", "name":"锦州市", },
                {"id":574, "code":"210800", "name":"营口市", },
                {"id":582, "code":"210900", "name":"阜新市", },
                {"id":591, "code":"211000", "name":"辽阳市",},
                {"id":600, "code":"211100", "name":"盘锦市",},
                {"id":606, "code":"211200", "name":"铁岭市", },
                {"id":615, "code":"211300", "name":"朝阳市", },
                {"id":624, "code":"211400", "name":"葫芦岛市", },
              ]},
            {"id":632, "code":"220000", "name":"吉林省", "city":[
                {"id":633, "code":"220100", "name":"长春市", },
                {"id":645, "code":"220200", "name":"吉林市", },
                {"id":656, "code":"220300", "name":"四平市", },
                {"id":664, "code":"220400", "name":"辽源市", },
                {"id":670, "code":"220500", "name":"通化市", },
                {"id":679, "code":"220600", "name":"白山市", },
                {"id":687, "code":"220700", "name":"松原市",},
                {"id":694, "code":"220800", "name":"白城市",},
                {"id":701, "code":"222400", "name":"延边朝鲜族自治州",}
              ]},
            {"id":710, "code":"230000", "name":"黑龙江省", "city":[
                {"id":711, "code":"230100", "name":"哈尔滨市",},
                {"id":731, "code":"230200", "name":"齐齐哈尔市", },
                {"id":749, "code":"230300", "name":"鸡西市",},
                {"id":760, "code":"230400", "name":"鹤岗市", },
                {"id":770, "code":"230500", "name":"双鸭山市", },
                {"id":780, "code":"230600", "name":"大庆市", },
                {"id":791, "code":"230700", "name":"伊春市", },
                {"id":810, "code":"230800", "name":"佳木斯市", },
                {"id":822, "code":"230900", "name":"七台河市",},
                {"id":828, "code":"231000", "name":"牡丹江市", },
                {"id":840, "code":"231100", "name":"黑河市", },
                {"id":848, "code":"231200", "name":"绥化市",},
                {"id":860, "code":"232700", "name":"大兴安岭地区",}
              ]},
            {"id":868, "code":"310000", "name":"上海市", "city":[
                {"id":869, "code":"310100", "name":"上海市", },
                {"id":888, "code":"310200", "name":"县",}
              ]},
            {"id":890, "code":"320000", "name":"江苏省", "city":[
                {"id":891, "code":"320100", "name":"南京市", },
                {"id":906, "code":"320200", "name":"无锡市", },
                {"id":916, "code":"320300", "name":"徐州市", },
                {"id":929, "code":"320400", "name":"常州市", },
                {"id":938, "code":"320500", "name":"苏州市", },
                {"id":951, "code":"320600", "name":"南通市", },
                {"id":961, "code":"320700", "name":"连云港市",},
                {"id":970, "code":"320800", "name":"淮安市", },
                {"id":980, "code":"320900", "name":"盐城市", },
                {"id":991, "code":"321000", "name":"扬州市", },
                {"id":1000, "code":"321100", "name":"镇江市", },
                {"id":1008, "code":"321200", "name":"泰州市", },
                {"id":1016, "code":"321300", "name":"宿迁市", }
              ]},
            {"id":1023, "code":"330000", "name":"浙江省", "city":[
                {"id":1024, "code":"330100", "name":"杭州市", },
                {"id":1039, "code":"330200", "name":"宁波市", },
                {"id":1052, "code":"330300", "name":"温州市", },
                {"id":1065, "code":"330400", "name":"嘉兴市", },
                {"id":1074, "code":"330500", "name":"湖州市",},
                {"id":1081, "code":"330600", "name":"绍兴市", },
                {"id":1089, "code":"330700", "name":"金华市",},
                {"id":1100, "code":"330800", "name":"衢州市", },
                {"id":1108, "code":"330900", "name":"舟山市",},
                {"id":1114, "code":"331000", "name":"台州市", },
                {"id":1125, "code":"331100", "name":"丽水市", }
              ]},
            {"id":1136, "code":"340000", "name":"安徽省", "city":[
                {"id":1137, "code":"340100", "name":"合肥市", },
                {"id":1146, "code":"340200", "name":"芜湖市", },
                {"id":1155, "code":"340300", "name":"蚌埠市", },
                {"id":1164, "code":"340400", "name":"淮南市",},
                {"id":1172, "code":"340500", "name":"马鞍山市",},
                {"id":1178, "code":"340600", "name":"淮北市",},
                {"id":1184, "code":"340700", "name":"铜陵市",},
                {"id":1190, "code":"340800", "name":"安庆市", },
                {"id":1203, "code":"341000", "name":"黄山市", },
                {"id":1212, "code":"341100", "name":"滁州市",},
                {"id":1222, "code":"341200", "name":"阜阳市", },
                {"id":1232, "code":"341300", "name":"宿州市", },
                {"id":1239, "code":"341400", "name":"巢湖市", },
                {"id":1246, "code":"341500", "name":"六安市", },
                {"id":1255, "code":"341600", "name":"亳州市", },
                {"id":1261, "code":"341700", "name":"池州市", },
                {"id":1267, "code":"341800", "name":"宣城市",}
              ]},
            {"id":1276, "code":"350000", "name":"福建省", "city":[
                {"id":1277, "code":"350100", "name":"福州市", },
                {"id":1292, "code":"350200", "name":"厦门市", },
                {"id":1300, "code":"350300", "name":"莆田市", },
                {"id":1307, "code":"350400", "name":"三明市",},
                {"id":1321, "code":"350500", "name":"泉州市",},
                {"id":1335, "code":"350600", "name":"漳州市", },
                {"id":1348, "code":"350700", "name":"南平市", },
                {"id":1360, "code":"350800", "name":"龙岩市",},
                {"id":1369, "code":"350900", "name":"宁德市", }
              ]},
            {"id":1380, "code":"360000", "name":"江西省", "city":[
                {"id":1381, "code":"360100", "name":"南昌市",},
                {"id":1392, "code":"360200", "name":"景德镇市",},
                {"id":1398, "code":"360300", "name":"萍乡市", },
                {"id":1405, "code":"360400", "name":"九江市", },
                {"id":1419, "code":"360500", "name":"新余市", },
                {"id":1423, "code":"360600", "name":"鹰潭市", },
                {"id":1428, "code":"360700", "name":"赣州市",},
                {"id":1448, "code":"360800", "name":"吉安市",},
                {"id":1463, "code":"360900", "name":"宜春市",},
                {"id":1475, "code":"361000", "name":"抚州市", },
                {"id":1488, "code":"361100", "name":"上饶市", }
              ]},
            {"id":1502, "code":"370000", "name":"山东省", "city":[
                {"id":1503, "code":"370100", "name":"济南市", },
                {"id":1515, "code":"370200", "name":"青岛市",},
                {"id":1529, "code":"370300", "name":"淄博市", },
                {"id":1539, "code":"370400", "name":"枣庄市", },
                {"id":1547, "code":"370500", "name":"东营市", },
                {"id":1554, "code":"370600", "name":"烟台市", },
                {"id":1568, "code":"370700", "name":"潍坊市",},
                {"id":1582, "code":"370800", "name":"济宁市", },
                {"id":1596, "code":"370900", "name":"泰安市", },
                {"id":1604, "code":"371000", "name":"威海市", },
                {"id":1610, "code":"371100", "name":"日照市",},
                {"id":1616, "code":"371200", "name":"莱芜市",},
                {"id":1620, "code":"371300", "name":"临沂市", },
                {"id":1634, "code":"371400", "name":"德州市", },
                {"id":1647, "code":"371500", "name":"聊城市", },
                {"id":1657, "code":"371600", "name":"滨州市",},
                {"id":1666, "code":"371700", "name":"菏泽市", }
              ]},
            {"id":1677, "code":"410000", "name":"河南省", "city":[
                {"id":1678, "code":"410100", "name":"郑州市", },
                {"id":1692, "code":"410200", "name":"开封市",},
                {"id":1704, "code":"410300", "name":"洛阳市", },
                {"id":1721, "code":"410400", "name":"平顶山市",},
                {"id":1733, "code":"410500", "name":"安阳市", },
                {"id":1744, "code":"410600", "name":"鹤壁市", },
                {"id":1751, "code":"410700", "name":"新乡市",},
                {"id":1765, "code":"410800", "name":"焦作市", },
                {"id":1777, "code":"410900", "name":"濮阳市", },
                {"id":1785, "code":"411000", "name":"许昌市", },
                {"id":1793, "code":"411100", "name":"漯河市", },
                {"id":1800, "code":"411200", "name":"三门峡市", },
                {"id":1808, "code":"411300", "name":"南阳市", },
                {"id":1823, "code":"411400", "name":"商丘市", },
                {"id":1834, "code":"411500", "name":"信阳市", },
                {"id":1846, "code":"411600", "name":"周口市",},
                {"id":1858, "code":"411700", "name":"驻马店市", },
              ]},
            {"id":1872, "code":"420000", "name":"湖北省", "city":[
                {"id":1873, "code":"420100", "name":"武汉市", },
                {"id":1888, "code":"420200", "name":"黄石市", },
                {"id":1896, "code":"420300", "name":"十堰市", },
                {"id":1906, "code":"420500", "name":"宜昌市",},
                {"id":1921, "code":"420600", "name":"襄樊市", },
                {"id":1932, "code":"420700", "name":"鄂州市", },
                {"id":1937, "code":"420800", "name":"荆门市",},
                {"id":1944, "code":"420900", "name":"孝感市", },
                {"id":1953, "code":"421000", "name":"荆州市", },
                {"id":1963, "code":"421100", "name":"黄冈市",},
                {"id":1975, "code":"421200", "name":"咸宁市",},
                {"id":1983, "code":"421300", "name":"随州市", },
                {"id":1987, "code":"422800", "name":"恩施土家族苗族自治州",},
              ]},
            {"id":2001, "code":"430000", "name":"湖南省", "city":[
                {"id":2002, "code":"430100", "name":"长沙市", },
                {"id":2013, "code":"430200", "name":"株洲市", },
                {"id":2024, "code":"430300", "name":"湘潭市",},
                {"id":2031, "code":"430400", "name":"衡阳市", },
                {"id":2045, "code":"430500", "name":"邵阳市", },
                {"id":2059, "code":"430600", "name":"岳阳市", },
                {"id":2070, "code":"430700", "name":"常德市", },
                {"id":2081, "code":"430800", "name":"张家界市", },
                {"id":2087, "code":"430900", "name":"益阳市",},
                {"id":2095, "code":"431000", "name":"郴州市",},
                {"id":2108, "code":"431100", "name":"永州市", },
                {"id":2121, "code":"431200", "name":"怀化市", },
                {"id":2135, "code":"431300", "name":"娄底市",},
                {"id":2142, "code":"433100", "name":"湘西土家族苗族自治州", }
              ]},
            {"id":2151, "code":"440000", "name":"广东省", "city":[
                {"id":2152, "code":"440100", "name":"广州市", },
                {"id":2166, "code":"440200", "name":"韶关市",},
                {"id":2178, "code":"440300", "name":"深圳市", },
                {"id":2186, "code":"440400", "name":"珠海市", },
                {"id":2191, "code":"440500", "name":"汕头市",},
                {"id":2200, "code":"440600", "name":"佛山市", },
                {"id":2207, "code":"440700", "name":"江门市",},
                {"id":2216, "code":"440800", "name":"湛江市",},
                {"id":2227, "code":"440900", "name":"茂名市", },
                {"id":2235, "code":"441200", "name":"肇庆市",},
                {"id":2245, "code":"441300", "name":"惠州市", },
                {"id":2252, "code":"441400", "name":"梅州市", },
                {"id":2262, "code":"441500", "name":"汕尾市",},
                {"id":2268, "code":"441600", "name":"河源市", },
                {"id":2276, "code":"441700", "name":"阳江市", },
                {"id":2282, "code":"441800", "name":"清远市", },
                {"id":2292, "code":"441900", "name":"东莞市",},
                {"id":2293, "code":"442000", "name":"中山市",},
                {"id":2294, "code":"445100", "name":"潮州市", },
                {"id":2299, "code":"445200", "name":"揭阳市", },
                {"id":2306, "code":"445300", "name":"云浮市",}
              ]},
            {"id":2313, "code":"450000", "name":"广西壮族自治区", "city":[
                {"id":2314, "code":"450100", "name":"南宁市", },
                {"id":2328, "code":"450200", "name":"柳州市", },
                {"id":2340, "code":"450300", "name":"桂林市",},
                {"id":2359, "code":"450400", "name":"梧州市",},
                {"id":2368, "code":"450500", "name":"北海市",},
                {"id":2374, "code":"450600", "name":"防城港市",},
                {"id":2380, "code":"450700", "name":"钦州市", },
                {"id":2386, "code":"450800", "name":"贵港市", },
                {"id":2393, "code":"450900", "name":"玉林市",},
                {"id":2401, "code":"451000", "name":"百色市",},
                {"id":2415, "code":"451100", "name":"贺州市", },
                {"id":2421, "code":"451200", "name":"河池市", },
                {"id":2434, "code":"451300", "name":"来宾市", },
                {"id":2442, "code":"451400", "name":"崇左市",}
              ]},
            {"id":2451, "code":"460000", "name":"海南省", "city":[
                {"id":2452, "code":"460100", "name":"海口市",},
                {"id":2458, "code":"460200", "name":"三亚市", },
                {"id":2461, "code":"469001", "name":"五指山市"},
                {"id":2462, "code":"469002", "name":"琼海市"},
                {"id":2463, "code":"469003", "name":"儋州市"},
                {"id":2464, "code":"469005", "name":"文昌市"},
                {"id":2465, "code":"469006", "name":"万宁市"},
                {"id":2466, "code":"469007", "name":"东方市"},
                {"id":2467, "code":"469021", "name":"定安县"},
                {"id":2468, "code":"469022", "name":"屯昌县"},
                {"id":2469, "code":"469023", "name":"澄迈县"},
                {"id":2470, "code":"469024", "name":"临高县"},
                {"id":2471, "code":"469025", "name":"白沙黎族自治县"},
                {"id":2472, "code":"469026", "name":"昌江黎族自治县"},
                {"id":2473, "code":"469027", "name":"乐东黎族自治县"},
                {"id":2474, "code":"469028", "name":"陵水黎族自治县"},
                {"id":2475, "code":"469029", "name":"保亭黎族苗族自治县"},
                {"id":2476, "code":"469030", "name":"琼中黎族苗族自治县"},
                {"id":2477, "code":"469031", "name":"西沙群岛"},
                {"id":2478, "code":"469032", "name":"南沙群岛"},
                {"id":2479, "code":"469033", "name":"中沙群岛的岛礁及其海域"}
              ]},
            {"id":2480, "code":"500000", "name":"重庆市", "city":[
                {"id":2481, "code":"500100", "name":"重庆市", },
              ]},
            {"id":2523, "code":"510000", "name":"四川省", "city":[
                {"id":2524, "code":"510100", "name":"成都市", },
                {"id":2545, "code":"510300", "name":"自贡市",},
                {"id":2553, "code":"510400", "name":"攀枝花市", },
                {"id":2560, "code":"510500", "name":"泸州市", },
                {"id":2569, "code":"510600", "name":"德阳市", },
                {"id":2577, "code":"510700", "name":"绵阳市", },
                {"id":2588, "code":"510800", "name":"广元市", },
                {"id":2597, "code":"510900", "name":"遂宁市",},
                {"id":2604, "code":"511000", "name":"内江市", },
                {"id":2611, "code":"511100", "name":"乐山市", },
                {"id":2624, "code":"511300", "name":"南充市", },
                {"id":2635, "code":"511400", "name":"眉山市",},
                {"id":2643, "code":"511500", "name":"宜宾市", },
                {"id":2655, "code":"511600", "name":"广安市", },
                {"id":2662, "code":"511700", "name":"达州市",},
                {"id":2671, "code":"511800", "name":"雅安市", },
                {"id":2681, "code":"511900", "name":"巴中市", },
                {"id":2687, "code":"512000", "name":"资阳市", },
                {"id":2693, "code":"513200", "name":"阿坝藏族羌族自治州", },
                {"id":2707, "code":"513300", "name":"甘孜藏族自治州",},
                {"id":2726, "code":"513400", "name":"凉山彝族自治州",}
              ]},
            {"id":2744, "code":"520000", "name":"贵州省", "city":[
                {"id":2745, "code":"520100", "name":"贵阳市", },
                {"id":2757, "code":"520200", "name":"六盘水市", },
                {"id":2762, "code":"520300", "name":"遵义市", },
                {"id":2778, "code":"520400", "name":"安顺市",},
                {"id":2786, "code":"522200", "name":"铜仁地区", },
                {"id":2797, "code":"522300", "name":"黔西南布依族苗族自治州", },
                {"id":2806, "code":"522400", "name":"毕节地区", },
                {"id":2815, "code":"522600", "name":"黔东南苗族侗族自治州",},
                {"id":2832, "code":"522700", "name":"黔南布依族苗族自治州", }
              ]},
            {"id":2845, "code":"530000", "name":"云南省", "city":[
                {"id":2846, "code":"530100", "name":"昆明市", },
                {"id":2862, "code":"530300", "name":"曲靖市",},
                {"id":2873, "code":"530400", "name":"玉溪市", },
                {"id":2884, "code":"530500", "name":"保山市", },
                {"id":2891, "code":"530600", "name":"昭通市",},
                {"id":2904, "code":"530700", "name":"丽江市", },
                {"id":2911, "code":"530800", "name":"普洱市", },
                {"id":2923, "code":"530900", "name":"临沧市", },
                {"id":2933, "code":"532300", "name":"楚雄彝族自治州", },
                {"id":2944, "code":"532500", "name":"红河哈尼族彝族自治州", },
                {"id":2958, "code":"532600", "name":"文山壮族苗族自治州",},
                {"id":2967, "code":"532800", "name":"西双版纳傣族自治州", },
                {"id":2971, "code":"532900", "name":"大理白族自治州", },
                {"id":2984, "code":"533100", "name":"德宏傣族景颇族自治州", },
                {"id":2990, "code":"533300", "name":"怒江傈僳族自治州", },
                {"id":2995, "code":"533400", "name":"迪庆藏族自治州", }
              ]},
            {"id":2999, "code":"540000", "name":"西藏自治区", "city":[
                {"id":3000, "code":"540100", "name":"拉萨市", },
                {"id":3010, "code":"542100", "name":"昌都地区", },
                {"id":3022, "code":"542200", "name":"山南地区", },
                {"id":3035, "code":"542300", "name":"日喀则地区", },
                {"id":3054, "code":"542400", "name":"那曲地区", },
                {"id":3065, "code":"542500", "name":"阿里地区", },
                {"id":3073, "code":"542600", "name":"林芝地区", }
              ]},
            {"id":3081, "code":"610000", "name":"陕西省", "city":[
                {"id":3082, "code":"610100", "name":"西安市", },
                {"id":3097, "code":"610200", "name":"铜川市", },
                {"id":3103, "code":"610300", "name":"宝鸡市",},
                {"id":3117, "code":"610400", "name":"咸阳市", },
                {"id":3133, "code":"610500", "name":"渭南市", },
                {"id":3146, "code":"610600", "name":"延安市", },
                {"id":3161, "code":"610700", "name":"汉中市",},
                {"id":3174, "code":"610800", "name":"榆林市", },
                {"id":3188, "code":"610900", "name":"安康市",},
                {"id":3200, "code":"611000", "name":"商洛市", }
              ]},
            {"id":3209, "code":"620000", "name":"甘肃省", "city":[
                {"id":3210, "code":"620100", "name":"兰州市",},
                {"id":3220, "code":"620200", "name":"嘉峪关市", },
                {"id":3222, "code":"620300", "name":"金昌市",},
                {"id":3226, "code":"620400", "name":"白银市",},
                {"id":3233, "code":"620500", "name":"天水市", },
                {"id":3242, "code":"620600", "name":"武威市", },
                {"id":3248, "code":"620700", "name":"张掖市",},
                {"id":3256, "code":"620800", "name":"平凉市", },
                {"id":3265, "code":"620900", "name":"酒泉市", },
                {"id":3274, "code":"621000", "name":"庆阳市", },
                {"id":3284, "code":"621100", "name":"定西市", },
                {"id":3293, "code":"621200", "name":"陇南市", },
                {"id":3304, "code":"622900", "name":"临夏回族自治州",},
                {"id":3313, "code":"623000", "name":"甘南藏族自治州", }
              ]},
            {"id":3322, "code":"630000", "name":"青海省", "city":[
                {"id":3323, "code":"630100", "name":"西宁市", },
                {"id":3332, "code":"632100", "name":"海东地区"},
                {"id":3339, "code":"632200", "name":"海北藏族自治州", },
                {"id":3344, "code":"632300", "name":"黄南藏族自治州",},
                {"id":3349, "code":"632500", "name":"海南藏族自治州",},
                {"id":3355, "code":"632600", "name":"果洛藏族自治州"},
                {"id":3362, "code":"632700", "name":"玉树藏族自治州"},
                {"id":3369, "code":"632800", "name":"海西蒙古族藏族自治州"}
              ]},
            {"id":3375, "code":"640000", "name":"宁夏回族自治区", "city":[
                {"id":3376, "code":"640100", "name":"银川市", },
                {"id":3384, "code":"640200", "name":"石嘴山市"},
                {"id":3389, "code":"640300", "name":"吴忠市", },
                {"id":3395, "code":"640400", "name":"固原市", },
                {"id":3402, "code":"640500", "name":"中卫市", }
              ]},
            {"id":3407, "code":"650000", "name":"新疆维吾尔自治区", "city":[
                {"id":3408, "code":"650100", "name":"乌鲁木齐市", },
                {"id":3418, "code":"650200", "name":"克拉玛依市", },
                {"id":3424, "code":"652100", "name":"吐鲁番地区",},
                {"id":3428, "code":"652200", "name":"哈密地区",},
                {"id":3432, "code":"652300", "name":"昌吉回族自治州", },
                {"id":3440, "code":"652700", "name":"博尔塔拉蒙古自治州", },
                {"id":3444, "code":"652800", "name":"巴音郭楞蒙古自治州", },
                {"id":3454, "code":"652900", "name":"阿克苏地区", },
                {"id":3464, "code":"653000", "name":"克孜勒苏柯尔克孜自治州", },
                {"id":3469, "code":"653100", "name":"喀什地区", },
                {"id":3482, "code":"653200", "name":"和田地区", },
                {"id":3491, "code":"654000", "name":"伊犁哈萨克自治州",},
                {"id":3502, "code":"654200", "name":"塔城地区", },
                {"id":3510, "code":"654300", "name":"阿勒泰地区", },
                {"id":3519, "code":"659001", "name":"石河子市"},
                {"id":3520, "code":"659002", "name":"阿拉尔市"},
                {"id":3521, "code":"659003", "name":"图木舒克市"},
                {"id":3522, "code":"659004", "name":"五家渠市"}
              ]},
            {"id":3523, "code":"710000", "name":"台湾省", "city":[
              ]},
            {"id":3524, "code":"810000", "name":"香港特别行政区", "city":[
              ]},
            {"id":3525, "code":"820000", "name":"澳门特别行政区", "city":[
              ]}
          ]
        },

      switchValue:false,
      beginPlace:'',
      endPlace:'',
      beginTime: '',
      checkList: [],
      trainTypeOptions: [{
        value: 'G',
        label: '高铁'
      }, {
        value: 'D',
        label: '动车'
      }, {
        value: 'K',
        label: '快速'
      },{
        value: 'T',
        label: '特快'
      }],
      trainType: '',
      seatTypeOptions: [{
        value: '0',
        label: '商务座'
      }, {
        value: '1',
        label: '一等座'
      }, {
        value: '2',
        label: '二等座'
      },{
        value: '3',
        label: '卧铺',
      }],
      seatType: '',

      dialogVisible2:false,
      ticketType:'',
      dialogVisible3:false,

      tableData: [],
      changeList:[],
      activeStep:0,

      //确认购票信息的表格
      verifyTable:[],
      summaryPrice:0,

      changeVerifyTable1:[],
      changeVerifyTable2:[]
    };
  },

  methods:{
    //输入建议
    querySearch(queryString, cb){
      let results = []
      let provinces = this.provinceAndCityData.province
      for(let i=0;i<provinces.length;i++){
        let citys = provinces[i].city
        for(let j=0;j<citys.length;j++){
          if(citys[j].name.includes(queryString)){
            let obj = {value:citys[j].name.substring(0,citys[j].name.length-1)}
            results.push(obj)
          }
        }
      }
      cb(results)
    },

    //关闭对话框
    handleClose(done) {
      this.$confirm('确认关闭？')
        .then(_ => {
          this.dialogVisible2 = false
          this.verifyTable = []
          this.summaryPrice = 0
          done();
        })
        .catch(_ => {});
    },

    //合并行
    objectSpanMethod({ row, column, rowIndex, columnIndex }) {
      if (columnIndex === 10 && this.switchValue === true) {
        if (rowIndex % 2 === 0) {
          return {
            rowspan: 2,
            colspan: 1
          };
        } else {

          return {
            rowspan: 0,
            colspan: 0
          };
        }
      }
    },
    //清空表单
    clearCheckList(){
      this.checkList = []
      this.tableData = []
    },
    //加载购票信息并且打开购票确认窗口
    loadInfo(info,index){
      if(this.ticketType !== ''){
        let seatRemain = 0;
        if(this.ticketType === '0')
          seatRemain = parseInt(info.businessSeat.remaining)
        else if(this.ticketType === '1')
          seatRemain = parseInt(info.firstSeat.remaining)
        else if(this.ticketType === '2')
          seatRemain = parseInt(info.secondSeat.remaining)
        else if(this.ticketType === '3')
          seatRemain = parseInt(info.sleeper.remaining)
        if(seatRemain > 0){
          this.dialogVisible2 = true
          let buyersList = sessionStorage.getItem('buyers').split(',')
          basePost.post('/ticket/recommend','tid='+info.trainID+'&startSname='+info.startStation+'&arriveSname='+info.endStation+'&type='+this.ticketType+'&num='+buyersList.length)
            .then(successResponse=>{
              let infoArray = JSON.parse(successResponse.data.description)
              let ticketPrice = 0;
              if(this.ticketType === '0')
                ticketPrice = info.businessSeat.price
              else if(this.ticketType === '1')
                ticketPrice = info.firstSeat.price
              else if(this.ticketType === '2')
                ticketPrice = info.secondSeat.price
              else if(this.ticketType === '3')
                ticketPrice = info.sleeper.price
              else
                ticketPrice = '请选择座位类型'
              for(let i = 0;i<buyersList.length;i++){
                let buyer = {
                  buyerID:buyersList[i],
                  trainID:info.trainID,
                  startStation:info.startStation,
                  endStation:info.endStation,
                  startDate:info.startDate,
                  endDate:info.endDate,
                  seatType:this.typeLabel,
                  seatInfo:infoArray[i]===null?'暂无座位':infoArray[i],
                  price:ticketPrice
                }
                this.summaryPrice += parseFloat(ticketPrice)
                this.verifyTable.push(buyer)
              }
            })
          if(this.switchValue)
            this.loadAnotherInfo(this.tableData[index+1],index+1)
        }else
          this.$message.warning('无座位空余')
      }else{
        this.$message.warning('请先选择座位类型')
      }
    },

    //加载换乘购票信息并且打开购票确认窗口
    loadAnotherInfo(info,index){
      if(this.ticketType !== ''){
        let seatRemain = 0;
        if(this.ticketType === '0')
          seatRemain = parseInt(info.businessSeat.remaining)
        else if(this.ticketType === '1')
          seatRemain = parseInt(info.firstSeat.remaining)
        else if(this.ticketType === '2')
          seatRemain = parseInt(info.secondSeat.remaining)
        else if(this.ticketType === '3')
          seatRemain = parseInt(info.sleeper.remaining)
        if(seatRemain > 0){
          let buyersList = sessionStorage.getItem('buyers').split(',')
          basePost.post('/ticket/recommend','tid='+info.trainID+'&startSname='+info.startStation+'&arriveSname='+info.endStation+'&type='+this.ticketType+'&num='+buyersList.length)
            .then(successResponse=>{
              let infoArray = JSON.parse(successResponse.data.description)
              let ticketPrice = 0;
              if(this.ticketType === '0')
                ticketPrice = info.businessSeat.price
              else if(this.ticketType === '1')
                ticketPrice = info.firstSeat.price
              else if(this.ticketType === '2')
                ticketPrice = info.secondSeat.price
              else if(this.ticketType === '3')
                ticketPrice = info.sleeper.price
              else
                ticketPrice = '请选择座位类型'
              for(let i = 0;i<buyersList.length;i++){
                let buyer = {
                  buyerID:buyersList[i],
                  trainID:info.trainID,
                  startStation:info.startStation,
                  endStation:info.endStation,
                  startDate:info.startDate,
                  endDate:info.endDate,
                  seatType:this.typeLabel,
                  seatInfo:infoArray[i]===null?'暂无座位':infoArray[i],
                  price:ticketPrice
                }
                this.summaryPrice += parseFloat(ticketPrice)
                this.verifyTable.push(buyer)
              }
            })
        }else
          this.$message.warning('无座位空余')
      }else{
        this.$message.warning('请先选择座位类型')
      }
    },

    //购票
    buyTicket(){
      this.dialogVisible2 = false
      this.dialogVisible3 = true
    },


    //预定
    async scheduled() {
      let finished = true
      let error = ''
      for (let i = 0; i < this.verifyTable.length; i++) {
        let infoArray = this.verifyTable[i].seatInfo.split(',')//格式为 1车,1排,1座
        let seatSequence = (parseInt(infoArray[1])-1)*5+parseInt(infoArray[2])
        let response = await basePost.post('/ticket/add', 'usercard='+this.verifyTable[i].buyerID+'&tid='+this.verifyTable[i].trainID+'&startSname='+this.verifyTable[i].startStation+'&arriveSname='+this.verifyTable[i].endStation+
          '&carriageSequence='+parseInt(infoArray[0])+'&seatSequence='+seatSequence+'&price='+this.verifyTable[i].price+'&paid='+'')
        if(response.data.type !== 0){
          finished = false
          error = this.verifyTable[i].buyerID
          break
        }
      }
      if(finished){
        this.$message.success('预定成功')
      }
      else
        this.$message.error(error+'已预定过本车次列车')
      this.dialogVisible2 = false//关闭窗口
      this.verifyTable = []
      this.summaryPrice = 0
    },


    //支付
    async payfor() {
      let finished = true
      let error = ''
      for (let i = 0; i < this.verifyTable.length; i++) {
        let infoArray = this.verifyTable[i].seatInfo.split(',')//格式为 1车,1排,1座
        let seatSequence = (parseInt(infoArray[1])-1)*5+parseInt(infoArray[2])
        let response = await basePost.post('/ticket/add', 'usercard='+this.verifyTable[i].buyerID+'&tid='+this.verifyTable[i].trainID+'&startSname='+this.verifyTable[i].startStation+'&arriveSname='+this.verifyTable[i].endStation+
          '&carriageSequence='+parseInt(infoArray[0])+'&seatSequence='+seatSequence+'&price='+this.verifyTable[i].price+'&paid='+'1')
        if(response.data.type !== 0){
          finished = false
          error = this.verifyTable[i].buyerID
          break
        }
      }
      if(finished){
        this.$message.success('购买成功')
      }
      else
        this.$message.error(error+'已购买过本车次列车')
      this.dialogVisible3 = false//关闭窗口
      this.verifyTable = []
      this.summaryPrice = 0
    },

    //改async签
    async modifyTicket() {
      let finished = true
      for (let i = 0; i < this.verifyTable.length; i++) {
        let infoArray = this.verifyTable[i].seatInfo.split(',')//格式为 1车,1排,1座
        let seatSequence = (parseInt(infoArray[1])-1)*5+parseInt(infoArray[2])
        let response = await basePost.post('/ticket/modify', 'usercard='+this.verifyTable[i].buyerID+'&tid='+this.verifyTable[i].trainID+'&startSname='+this.verifyTable[i].startStation+'&arriveSname='+this.verifyTable[i].endStation+
          '&carriageSequence='+parseInt(infoArray[0])+'&seatSequence='+seatSequence+'&price='+this.verifyTable[i].price)
        if(response.data.type !== 0){
          finished = false
          break
        }
      }
      if(finished)
        this.$message.success('改签成功')
      else
        this.$message.error('未购买过本车次列车')
      this.dialogVisible2 = false//关闭窗口
      this.verifyTable = [];//清空信息确认窗口
    },

    //清空购票信息
    cleanInfo(){
      this.verifyTable = []
      this.summaryPrice = 0
      this.dialogVisible3 = false;
    },

    //换乘的不同选项区分颜色
    tableRowClassName({row, rowIndex}) {
      if(this.switchValue === true){
        if (rowIndex % 4 === 0 || rowIndex % 4 === 1) {
          return 'way1';
        } else
          return 'way2';
      }else
        return '';
    },

    //进行火车票查询
    search() {
      //清空列表
      this.tableData = []
      /**
       * 直达查询
       */
      if (!this.switchValue) {
        basePost.post('/station/specificQuery', 'startStation=' + this.beginPlace + '&endStation=' + this.endPlace + '&date=' + this.beginTime)
          .then(successResponse => {
            if (successResponse.status === 200 && successResponse.data.type === 0) {
              let infoArray = JSON.parse(successResponse.data.description)//字符串数组
              for (let i = 0; i < infoArray.length; i++) {
                let infos = infoArray[i].slice(1, infoArray[i].length - 1).split(',')//去除头尾的双引号
                let trainInfo = {
                  trainID: infos[0],
                  startStation: infos[1].trim(),
                  endStation: infos[3].trim(),
                  startDate: infos[5].trim(),
                  endDate: infos[6].trim(),
                  businessSeat: {
                    remaining: infos[9].trim(),
                    price: infos[7] * 3,
                  },
                  firstSeat: {
                    remaining: infos[10].trim(),
                    price: infos[7] * 2,
                  },
                  secondSeat: {
                    remaining: infos[11].trim(),
                    price: infos[7],
                  },
                  sleeper: {
                    remaining: infos[12].trim(),
                    price: infos[7] * 1.5,
                  },
                  mileage: infos[8].trim(),
                  state: Date.now() < infos[5] ? '未到站' : '已发车',
                }
                //查询中间站
                basePost.post('/station/transferQuery', 'tid=' + infos[0] + '&start_sid=' + infos[2] + '&end_sid=' + infos[4])
                  .then(successResponse => {
                    trainInfo.activities = JSON.parse(successResponse.data.description)
                  })
                //根据座位进行筛选
                if (this.seatType !== '') {
                  let seatTypeText = ''
                  switch (this.seatType) {
                    case '0':
                      seatTypeText = 'businessSeat'
                      break
                    case '1':
                      seatTypeText = 'firstSeat'
                      break
                    case '2':
                      seatTypeText = 'secondSeat'
                      break
                    case '3':
                      seatTypeText = 'sleeper'
                      break
                  }
                  if (trainInfo.trainID.indexOf(this.trainType) !== -1 && trainInfo[seatTypeText].remaining > 0) {
                    this.tableData.push(trainInfo)
                  }
                } else {
                  //对座位没要求 根据列车种类进行筛选
                  if (trainInfo.trainID.indexOf(this.trainType) !== -1) {
                    this.tableData.push(trainInfo)
                  }
                }
              }
              this.$message.success('查询成功')
              this.tableData.sort((a,b)=> {
                if(a.startDate>b.startDate)
                  return 1
                else if(a.startDate<b.startDate)
                  return -1
                else
                  return 0
              })
            } else {
              this.$message.warning('暂无列车安排或输入数据格式有误')
            }
          })
          .catch(failResponse => {
            this.$message.error('系统错误')
          })
      }
      /**
       * 进行换乘查询
       */
      else {
        basePost.post('/station/changeQuery', 'startStation=' + this.beginPlace + '&endStation=' + this.endPlace + '&date=' + this.beginTime)
          .then(async successResponse => {
            if (successResponse.status === 200 && successResponse.data.type === 0) {
              let infoArray = JSON.parse(successResponse.data.description)//取出的是一个String数组,表示几种换乘思路
              let time = []//时间评价数组
              let mile = []//路程评价函数
              let cost = []//价格评价函数
              for (let i = 0; i < infoArray.length; i++) {
                time[i] = 0;
                mile[i] = 0;
                cost[i] = 0;
                let changeInfo = infoArray[i].split(',')//去除头尾的双引号"K1153,1,北京,4,上海,2021-08-09,2021-08-09,T11,1,上海,3,福州,2021-08-14,2021-08-16"
                for (let j = 0; j < 8; j += 7) {
                  //使用同步axios请求！！
                  let successResponse = await basePost.post('/station/specificQuery', 'startStation=' + changeInfo[j + 2] + '&endStation=' + changeInfo[j + 4] + '&date=' + changeInfo[j + 5] + '&tid=' + changeInfo[j])
                      let singleArray = JSON.parse(successResponse.data.description)//取出的是换乘第一步的列车信息String数组，元素只有一个
                      let infos = singleArray[0].slice(1, singleArray[0].length - 1).split(',')
                      //计算换乘每一步的价格
                      cost[i] += parseFloat(infos[7])//基价
                      let trainInfo = {
                        trainID: infos[0],
                        startStation: infos[1].trim(),
                        endStation: infos[3].trim(),
                        startDate: infos[5].trim(),
                        endDate: infos[6].trim(),
                        businessSeat: {
                          remaining: infos[9].trim(),
                          price: infos[7] * 3,
                        },
                        firstSeat: {
                          remaining: infos[10].trim(),
                          price: infos[7] * 2,
                        },
                        secondSeat: {
                          remaining: infos[11].trim(),
                          price: infos[7],
                        },
                        sleeper: {
                          remaining: infos[12].trim(),
                          price: infos[7] * 1.5,
                        },
                        mileage: infos[8].trim(),
                        state: Date.now() < infos[5] ? '未到站' : '已发车',
                      }
                      //查询中间站
                      let successResponse1 = await basePost.post('/station/transferQuery', 'tid=' + infos[0] + '&start_sid=' + infos[2] + '&end_sid=' + infos[4])
                          trainInfo.activities = JSON.parse(successResponse1.data.description)
                          //计算换乘每一步所需的毫秒数
                          let date1 = new Date(trainInfo.activities[trainInfo.activities.length - 1].arrive_date.replace(/\-/g, "/"));
                          let date2 = new Date(trainInfo.activities[0].arrive_date.replace(/\-/g, "/"));
                          time[i] += date1 - date2
                      //计算换乘每一步的里程数
                      mile[i] += parseInt(trainInfo.mileage)
                      this.tableData.push(trainInfo)
                }//2次循环结束,将两个换乘步骤放入表格中
              }//所有列车信息已填入
              //收集评价数据,进行筛选
              if (this.checkList[0] === '最快时间'){
                let minIndex = time.indexOf(Math.min(...time))
                let minTableDate1 = this.tableData[minIndex*2]
                let minTableDate2 = this.tableData[minIndex*2+1]
                this.tableData = []
                this.tableData.push(minTableDate1,minTableDate2)
              }else if(this.checkList[0] === '最短路程'){
                let minIndex = mile.indexOf(Math.min(...mile))
                let minTableDate1 = this.tableData[minIndex*2]
                let minTableDate2 = this.tableData[minIndex*2+1]
                this.tableData = []
                this.tableData.push(minTableDate1,minTableDate2)
              }else if(this.checkList[0] === '最低价格'){
                let minIndex = cost.indexOf(Math.min(...cost))
                let minTableDate1 = this.tableData[minIndex*2]
                let minTableDate2 = this.tableData[minIndex*2+1]
                this.tableData = []
                this.tableData.push(minTableDate1,minTableDate2)
              }

            }
            else {
              this.$message.warning('查询结果为空或输入数据格式有误')
            }
          })
          .catch(failResponse => {
            this.$message.error('系统错误')
          })
      }
    },

    //选择购买票的种类的选择框的change函数
    changeType(type){
      this.ticketType = type
    },
  },

  computed:{
    typeLabel: function (){
      if(this.ticketType === '0')
        return '商务座'
      else if (this.ticketType === '1')
        return '一等座'
      else if (this.ticketType === '2')
        return '二等座'
      else if (this.ticketType === '3')
        return '卧铺'
      else if (this.ticketType === '')
        return ''
    }
  }
}
</script>

<style scoped>
#buyTicket-Outer{
  background: white;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  padding: 20px 20px;
}
.box-card {
    width: calc(100% - 40px);
    margin: 10px 0;
}
#buyTicket-Form{
  display: flex;
  flex-direction: row;
}
#Form-Left{
  display: flex;
  justify-content: center;
  align-items: center;
  width: 20%;
  border-right: #00c6ff 1px solid;
}
#Form-Middle{
  width: 60%;
  margin-left: 5px;
  margin-right: 5px;
}
#Form-Right{
  display: flex;
  flex-direction: row;
  justify-content: flex-end;
  width: 15%;
  border-left: #00c6ff 1px solid;
}
.Form-item{
  display: flex;
  flex-direction: row;
  margin: 5px 10px;
  align-items: center;
  justify-content: flex-start;
}
.Form-text{
  margin-right: 5px;
  white-space:nowrap
}
.table-content{
  display: flex;
  flex-direction: column;
}
.el-table >>> .way1 {
  background: oldlace;
}
.el-table >>> .way2 {
  background: #f0f9eb;
}
</style>
