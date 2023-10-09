//+------------------------------------------------------------------+
//|                                              BotQuanLyVon_v3.mq4 |
//|                                  Copyright 2023, MetaQuotes Ltd. |
//|      Có 3 kiểu vào lệnh: buy, sell, sw, dca nếu ngược chiều lệnh |
//|      Cắt lệnh theo cụm                                           |
//|      Nếu thêm lệnh bằng tay sẽ trung bình giá lại rồi sửa TP     |
//|                                             https://www.mql5.com |
//+------------------------------------------------------------------+
#property copyright "Copyright 2023, MetaQuotes Ltd."
#property link      "https://www.mql5.com"
#property version   "3.01"
#property strict
//+------------------------------------------------------------------+
//| Expert initialization function                                   |
//+------------------------------------------------------------------+
int Corner = 0;
int Move_X = 0;
int Move_Y = 0;
string B00000 = "============";
string Font_Type = "Arial Bold";
color Font_Color = White;
int Font_Size = 10;
string B00001 = "============";
int Button_Width = 50;
color Button_Color  = Navy;
double soLotBuy, soLotSell;
int countOrderHand = 0;

enum typeOfTrade
  {
   BUY = 1,
   SELL = 2,
   BUY_SELL = 3,
  };

extern typeOfTrade   kieuLenh       = BUY; // Chon chien luoc
input int            spread   = 100; // Spread
input int            minEquity = 0; // Min equity de stop
input int            maxEquity = 100000; // Max equity de stop
//---------------------------------------------
input double         khoiLuongLenh = 0.05; // Khoi luong
input int            giaTriX = 5; // Gia tri X
input double         khoiLuongTang  = 0; // Sau X lenh KL tang (lot)
//---------------------------------------------
input int            TP = 1000;
input int            giaTriY = 5; // Gia tri Y
input int            tpTang  = 0; // Sau Y lenh TP tang them (pip)
//---------------------------------------------
input int            khoangCachVaoLenhMoi = 1000 ; // Khoang cach DCA
input int            giaTriZ = 5; // Gia tri Z
input int            dcaTang  = 0; // Sau Z lenh DCA tang them (pip)
//---------------------------------------------
input double         capSoNhanVol = 0; // He so nhan khoi luong
//---------------------------------------------
input double         giaTriMin = 0; // Vung vao lenh min
input double         giaTriMax = 3000; // Vung vao lenh max
//---------------------------------------------
input int            maxCountBuy = 100; // Max so lenh buy
input int            maxCountSell = 100; // Max so lenh sell
input double         maxVol = 0.5; // Max volumn moi lenh (lot)
input int            magicNumber = 1; //Magic number


double maxPrice, minPrice, tpPoint;
bool stopBotFlag;
double khoiLuongLenhRealBuy, khoiLuongLenhRealSell;
int soLenhBuy, soLenhSell, khoangCachVaoLenhMoiReal;
int SL = NULL;

//+------------------------------------------------------------------+
//|                                                                  |
//+------------------------------------------------------------------+
int OnInit()
  {
   CreateButtons();
   resetValue();
   return(INIT_SUCCEEDED);
//---
  }
//+------------------------------------------------------------------+
//| Expert deinitialization function                                 |
//+------------------------------------------------------------------+
void OnDeinit(const int reason)
  {
   DeleteButtons();
   deleteComment();
  }

//+------------------------------------------------------------------+
//|                                                                  |
//+------------------------------------------------------------------+
void resetValue()
  {
   stopBotFlag = false;
  }

//+------------------------------------------------------------------+
//|                                                                  |
//+------------------------------------------------------------------+
void OnChartEvent(const int id, const long &lparam, const double &dparam, const string &sparam)
  {
   ResetLastError();
   if(id == CHARTEVENT_OBJECT_CLICK)
     {
      if(sparam=="STOP_BUTTON")
        {
         CloseAll_Button();
        }
      if(sparam=="START_BUTTON")
        {
         resetValue();
        }
      if(sparam=="STOP_THIS_CHART_BUTTON")
        {
         CloseAllThisChart_Button();
        }
     }
  }

//+------------------------------------------------------------------+
//|                                                                  |
//+------------------------------------------------------------------+
void CloseAll_Button()
  {
   stopBotFlag = true;
   CloseAllOrders();
  }

//+------------------------------------------------------------------+
//|                                                                  |
//+------------------------------------------------------------------+
void CloseAllThisChart_Button()
  {
   stopBotFlag = true;
   CloseAllOrdersOnThisChart();
  }

//+------------------------------------------------------------------+
//| Expert tick function                                             |
//+------------------------------------------------------------------+
void OnTick()
  {
   CommentsONchart();
   if(stopBotFlag == false)
     {
      double marketSpread = MarketInfo(Symbol(), MODE_SPREAD);
      if(AccountEquity() > minEquity && AccountEquity() < maxEquity)
        {
         // Start
         if(spread >= marketSpread && giaTriMin <= Bid && giaTriMax >= Bid)
           {
            // Type = BUY
            if(kieuLenh == BUY)
              {
               buy();
              }
            // Type = SELL
            if(kieuLenh == SELL)
              {
               sell();
              }
            // Type = BUY_SELL
            if(kieuLenh == BUY_SELL)
              {
               buy();
               sell();
              }
           }
        }
      else
        {
         CloseAllOrders();
        }
     }
  }

//+------------------------------------------------------------------+
//|                                                                  |
//+------------------------------------------------------------------+
void CloseAllOrders()
  {
// Update the exchange rates before closing the orders.
   RefreshRates();

// Start a loop to scan all the orders.
// The loop starts from the last order, proceeding backwards; Otherwise it would skip some orders.
   for(int i = (OrdersTotal() - 1); i >= 0; i--)
     {
      // If the order cannot be selected, throw and log an error.
      if(OrderSelect(i, SELECT_BY_POS, MODE_TRADES) == false)
        {
         Print("ERROR - Unable to select the order - ", GetLastError());
         break;
        }

      // Create the required variables.
      // Result variable - to check if the operation is successful or not.
      bool res = false;

      // Allowed Slippage - the difference between current price and close price.
      int Slippage = 0;

      // Bid and Ask prices for the instrument of the order.
      double BidPrice = MarketInfo(OrderSymbol(), MODE_BID);
      double AskPrice = MarketInfo(OrderSymbol(), MODE_ASK);

      // Closing the order using the correct price depending on the type of order.
      if(OrderType() == OP_BUY)
        {
         res = OrderClose(OrderTicket(), OrderLots(), BidPrice, Slippage);
        }
      else
         if(OrderType() == OP_SELL)
           {
            res = OrderClose(OrderTicket(), OrderLots(), AskPrice, Slippage);
           }

      // If there was an error, log it.
      if(res == false)
         Print("ERROR - Unable to close the order - ", OrderTicket(), " - ", GetLastError());
     }
  }
//+------------------------------------------------------------------+
//|                                                                  |
//+------------------------------------------------------------------+
bool existOrder(string type)
  {
   if(type == "BUY")
     {
      for(int trade = OrdersTotal()-1; trade >= 0; trade--)
        {
         if(!OrderSelect(trade,SELECT_BY_POS,MODE_TRADES))
            continue;
         if(OrderType() == OP_BUY && OrderMagicNumber() == magicNumber)
            return true;
        }
     }
   if(type == "SELL")
     {
      for(int trade = OrdersTotal()-1; trade >= 0; trade--)
        {
         if(!OrderSelect(trade,SELECT_BY_POS,MODE_TRADES))
            continue;
         if(OrderType() == OP_SELL && OrderMagicNumber() == magicNumber)
            return true;
        }
     }
   return false;
  }
//+------------------------------------------------------------------+

//+------------------------------------------------------------------+
void CreateButtons()
  {
   if(!ButtonCreate(0,"STOP_BUTTON", 0, Move_X + 400, Move_Y + 20, Button_Width, 28, Corner, "Stop", Font_Type, Font_Size, Font_Color, clrRed, clrBlack))
      return;
   if(!ButtonCreate(0,"START_BUTTON",0,Move_X + 340, Move_Y + 20, Button_Width, 28, Corner, "Start", Font_Type, Font_Size, Font_Color, clrGreen, clrBlack))
      return;
   if(!ButtonCreate(0,"STOP_THIS_CHART_BUTTON", 0, Move_X + 460, Move_Y + 20, Button_Width + 60, 28, Corner, "Stop This Chart", Font_Type, Font_Size, Font_Color, clrYellowGreen, clrBlack))
      return;
   ChartRedraw();
  }


//+------------------------------------------------------------------+
//|                                                                  |
//+------------------------------------------------------------------+
bool ButtonCreate(const long chart_ID = 0, const string name = "Button", const int sub_window = 0, const int x = 0, const int y = 0, const int width = 500,
                  const int height = 18, int corner = 0, const string text = "Button", const string font = "Arial Bold",
                  const int font_size = 10, const color clr = clrBlack, const color back_clr = White, const color border_clr = clrNONE,
                  const bool state = false, const bool back = false, const bool selection = false, const bool hidden = true, const long z_order = 0)
  {
   ResetLastError();
   if(!ObjectCreate(chart_ID,name, OBJ_BUTTON, sub_window, 0, 0))
     {
      Print(__FUNCTION__, ": Unable to create the button! Error code = ", GetLastError());
      return(false);
     }
   ObjectSetInteger(chart_ID, name, OBJPROP_XDISTANCE, x);
   ObjectSetInteger(chart_ID, name, OBJPROP_YDISTANCE, y);
   ObjectSetInteger(chart_ID, name, OBJPROP_XSIZE, width);
   ObjectSetInteger(chart_ID, name, OBJPROP_YSIZE, height);
   ObjectSetInteger(chart_ID, name, OBJPROP_CORNER, corner);
   ObjectSetInteger(chart_ID, name, OBJPROP_FONTSIZE, font_size);
   ObjectSetInteger(chart_ID, name, OBJPROP_COLOR, clr);
   ObjectSetInteger(chart_ID, name, OBJPROP_BGCOLOR, back_clr);
   ObjectSetInteger(chart_ID, name, OBJPROP_BORDER_COLOR, border_clr);
   ObjectSetInteger(chart_ID, name, OBJPROP_BACK, back);
   ObjectSetInteger(chart_ID, name, OBJPROP_STATE, state);
   ObjectSetInteger(chart_ID, name, OBJPROP_SELECTABLE, selection);
   ObjectSetInteger(chart_ID, name, OBJPROP_SELECTED, selection);
   ObjectSetInteger(chart_ID, name, OBJPROP_HIDDEN, hidden);
   ObjectSetInteger(chart_ID, name, OBJPROP_ZORDER,z_order);
   ObjectSetString(chart_ID, name, OBJPROP_TEXT, text);
   ObjectSetString(chart_ID, name, OBJPROP_FONT, font);
   return(true);
  }

//+------------------------------------------------------------------+
//|                                                                  |
//+------------------------------------------------------------------+
void DeleteButtons()
  {
   ButtonDelete(0, "STOP_BUTTON");
   ButtonDelete(0, "START_BUTTON");
   ButtonDelete(0, "STOP_THIS_CHART_BUTTON");
  }

//+------------------------------------------------------------------+
//|                                                                  |
//+------------------------------------------------------------------+
bool ButtonDelete(const long chart_ID = 0, const string name = "Button")
  {
   ResetLastError();
   if(!ObjectDelete(chart_ID,name))
     {
      Print(__FUNCTION__, ": Unable to delete the button! Error code = ", GetLastError());
      return(false);
     }
   return(true);
  }
//+------------------------------------------------------------------+

//+------------------------------------------------------------------+
//|                                                                  |
//+------------------------------------------------------------------+
double minMax(string orderType)
  {
   double min=1000000,max=0;
   for(int trade = OrdersTotal()-1; trade >= 0; trade--)
     {
      if(!OrderSelect(trade,SELECT_BY_POS,MODE_TRADES))
         continue;
      if(OrderOpenPrice() > max && OrderType() == OP_SELL && OrderMagicNumber() == magicNumber)
         max = OrderOpenPrice();
      if(OrderOpenPrice() < min && OrderType() == OP_BUY && OrderMagicNumber() == magicNumber)
         min = OrderOpenPrice();
     }
   if(orderType == "BUY")
     {
      return min;
     }
   else
     {
      return max;
     }
  }
//+------------------------------------------------------------------+
void buy()
  {
   soLenhBuy = countOrder("BUY");
   int ticket;
   tpPoint = TP + MathFloor((soLenhBuy + 0.0)/giaTriY)*tpTang;
   if(soLenhBuy < maxCountBuy)
     {
      if(capSoNhanVol != 0)
        {
         khoiLuongLenhRealBuy = NormalizeDouble(khoiLuongLenh * MathPow(capSoNhanVol, soLenhBuy), 2);
        }
      else
        {
         khoiLuongLenhRealBuy = khoiLuongLenh + MathFloor((soLenhBuy + 0.0)/giaTriX)*khoiLuongTang;
        }
      if(khoiLuongLenhRealBuy > maxVol)
        {
         khoiLuongLenhRealBuy = maxVol;
        }
      khoangCachVaoLenhMoiReal = khoangCachVaoLenhMoi + MathFloor((soLenhBuy + 0.0)/giaTriZ)*dcaTang;
      if(existOrder("BUY") == false)
        {
         ticket = OrderSend(Symbol(),OP_BUY,khoiLuongLenhRealBuy,Bid,500,SL,NULL,NULL,magicNumber,0,clrRed);
         OrderSelect(ticket, SELECT_BY_TICKET, MODE_TRADES);
         OrderModify(ticket, OrderOpenPrice(),NULL, OrderOpenPrice()+ TP* Point,0);
        }

      minPrice = minMax("BUY");
      if(minPrice - khoangCachVaoLenhMoiReal*Point >= Bid)
        {
         OrderSend(Symbol(),OP_BUY,khoiLuongLenhRealBuy,Bid,500,SL,NULL,NULL,magicNumber,0,clrRed);
         editTpAllOrder("BUY", tpPoint);
        }
     }

//Sửa TP nếu thêm lệnh/đóng lệnh bằng tay
   int tmp = countOrderWithHand("BUY");
   if(countOrderHand != tmp)
     {
      countOrderHand = tmp;
      editTpAllOrder("BUY", tpPoint);
     }
  }
//+------------------------------------------------------------------+

//+------------------------------------------------------------------+
//|                                                                  |
//+------------------------------------------------------------------+
void sell()
  {
   soLenhSell = countOrder("SELL");
   int ticket;
   tpPoint = TP + MathFloor((soLenhSell + 0.0)/giaTriY)*tpTang;
   if(soLenhSell < maxCountSell)
     {
      if(capSoNhanVol != 0)
        {
         khoiLuongLenhRealSell = NormalizeDouble(khoiLuongLenh * MathPow(capSoNhanVol, soLenhSell), 2);
        }
      else
        {
         khoiLuongLenhRealSell = khoiLuongLenh + MathFloor((soLenhSell + 0.0)/giaTriX)*khoiLuongTang;
        }
      if(khoiLuongLenhRealSell > maxVol)
        {
         khoiLuongLenhRealSell = maxVol;
        }
      khoangCachVaoLenhMoiReal = khoangCachVaoLenhMoi + MathFloor((soLenhSell + 0.0)/giaTriZ)*dcaTang;
      if(existOrder("SELL") == false)
        {
         ticket = OrderSend(Symbol(),OP_SELL,khoiLuongLenhRealSell,Bid,500,SL,NULL,NULL,magicNumber,0,clrRed);
         OrderSelect(ticket, SELECT_BY_TICKET, MODE_TRADES);
         OrderModify(ticket, OrderOpenPrice(),NULL, OrderOpenPrice()- TP* Point,0);
        }
      maxPrice = minMax("SELL");
      if(maxPrice + khoangCachVaoLenhMoiReal*Point <= Bid)
        {
         OrderSend(Symbol(),OP_SELL,khoiLuongLenhRealSell,Bid,500,SL,NULL,NULL,magicNumber,0,clrRed);
         editTpAllOrder("SELL", tpPoint);
        }
     }

//Sửa TP nếu thêm lệnh/đóng lệnh bằng tay
   int tmp = countOrderWithHand("SELL");
   if(countOrderHand != tmp)
     {
      countOrderHand = tmp;
      editTpAllOrder("SELL", tpPoint);
     }
  }
//+------------------------------------------------------------------+

//+------------------------------------------------------------------+
//|                                                                  |
//+------------------------------------------------------------------+
int countOrder(string type)
  {
   int countSell = 0;
   int countBuy = 0;
   for(int trade = OrdersTotal()-1; trade >= 0; trade--)
     {
      if(!OrderSelect(trade,SELECT_BY_POS,MODE_TRADES))
         continue;
      if(OrderMagicNumber() == magicNumber && OrderType() == OP_SELL)
        {
         countSell = countSell + 1;
        }

      if(OrderMagicNumber() == magicNumber && OrderType() == OP_BUY)
        {
         countBuy = countBuy + 1;
        }
     }
   if(type == "BUY")
     {
      return countBuy;
     }
   if(type == "SELL")
     {
      return countSell;
     }
   return 0;
  }
//+------------------------------------------------------------------+
void CommentsONchart()
  {
   countLot();
   string textcomment[8]; // 8 refers to the number of lines of text
   textcomment[0]="Trading information";
   textcomment[1]="---------------------------------------";
   textcomment[2]="Magic Number = " + magicNumber;
   textcomment[3]="So lot buy = " + DoubleToStr(soLotBuy,2);
   textcomment[4]="So lot sell = " + DoubleToStr(soLotSell,2);
   textcomment[5]="Trang thai hien tai = " + DoubleToStr(AccountEquity()-AccountBalance(),2);
   if(soLotBuy != soLotSell)
     {
      textcomment[6]="Gia hoa von = " + DoubleToStr(-(AccountEquity()-AccountBalance())/((soLotBuy-soLotSell)*100),2);
     }
   else
     {
      textcomment[6]="Can lenh";
     }
   textcomment[7]="---------------------------------------";

   int z=1;
   int k=25; // Shifts the whole block of text up or down
   while(z<=7)  //z must be equal to or larger than the textcomment[8] in this case 8 for 8 lines of test
     {
      if(StringLen(textcomment[z]) < 1)
        {
         z++;
        }
      else
        {
         color textcol = Gold;
         string font = "Tahoma";
         int size = 8;
         string ChartText = DoubleToStr(z, 0);
         ObjectCreate(ChartText, OBJ_LABEL, 0, 0, 0);
         ObjectSetText(ChartText, textcomment[z], size, font, textcol);
         ObjectSet(ChartText, OBJPROP_CORNER, 1);   // controls the corner the text is put into 0=top left 1=topright 2=bottom left 3=bottom right
         ObjectSet(ChartText, OBJPROP_XDISTANCE, 8);//controls distance text block is from margin
         ObjectSet(ChartText, OBJPROP_YDISTANCE, k);
         z++;
         k=k+15;// bigger the number the larger the gap between the lines of text
        }
     }
  }
//+------------------------------------------------------------------+
//+------------------------------------------------------------------+
void deleteComment()
  {
   int z=1;
   while(z<=8)
     {
      string ChartText = DoubleToStr(z, 0); // delete function to remove text when ea is removed from the chart
      ObjectDelete(ChartText);
      z++;

     }
  }
//+------------------------------------------------------------------+

//+------------------------------------------------------------------+
//|                                                                  |
//+------------------------------------------------------------------+
void countLot()
  {
   soLotSell = 0.0;
   soLotBuy = 0.0;
   for(int trade = OrdersTotal()-1; trade >= 0; trade--)
     {
      if(!OrderSelect(trade,SELECT_BY_POS,MODE_TRADES))
         continue;
      if(OrderType() == OP_SELL)
        {
         soLotSell = soLotSell + OrderLots();
        }

      if(OrderType() == OP_BUY)
        {
         soLotBuy = soLotBuy + OrderLots();
        }
     }
  }
//+------------------------------------------------------------------+

//+------------------------------------------------------------------+
//|                                                                  |
//+------------------------------------------------------------------+
void CloseAllOrdersOnThisChart()
  {
// Update the exchange rates before closing the orders.
   RefreshRates();

// Start a loop to scan all the orders.
// The loop starts from the last order, proceeding backwards; Otherwise it would skip some orders.
   for(int i = (OrdersTotal() - 1); i >= 0; i--)
     {
      // If the order cannot be selected, throw and log an error.
      if(OrderSelect(i, SELECT_BY_POS, MODE_TRADES) == false)
        {
         Print("ERROR - Unable to select the order - ", GetLastError());
         break;
        }

      // Create the required variables.
      // Result variable - to check if the operation is successful or not.
      bool res = false;

      // Allowed Slippage - the difference between current price and close price.
      int Slippage = 0;

      // Bid and Ask prices for the instrument of the order.
      double BidPrice = MarketInfo(OrderSymbol(), MODE_BID);
      double AskPrice = MarketInfo(OrderSymbol(), MODE_ASK);

      // Closing the order using the correct price depending on the type of order.
      if(OrderType() == OP_BUY && OrderMagicNumber() == magicNumber)
        {
         res = OrderClose(OrderTicket(), OrderLots(), BidPrice, Slippage);
        }
      else
         if(OrderType() == OP_SELL && OrderMagicNumber() == magicNumber)
           {
            res = OrderClose(OrderTicket(), OrderLots(), AskPrice, Slippage);
           }

      // If there was an error, log it.
      if(res == false)
         Print("ERROR - Unable to close the order - ", OrderTicket(), " - ", GetLastError());
     }
  }
//+------------------------------------------------------------------+

//+------------------------------------------------------------------+
//|                                                                  |
//+------------------------------------------------------------------+
void editTpAllOrder(string type, double tpPoint)
  {
   double priceTotal = 0;
   double countLot = 0;
   for(int trade = OrdersTotal()-1; trade >= 0; trade--)
     {
      if(!OrderSelect(trade,SELECT_BY_POS,MODE_TRADES))
         continue;
      if((OrderMagicNumber() == magicNumber || OrderMagicNumber() == 0) && OrderType() == OP_SELL && type == "SELL")
        {
         countLot = countLot + OrderLots();
         priceTotal = priceTotal + OrderLots()* OrderOpenPrice();
        }

      if((OrderMagicNumber() == magicNumber || OrderMagicNumber() == 0) && OrderType() == OP_BUY && type == "BUY")
        {
         countLot = countLot + OrderLots();
         priceTotal = priceTotal + OrderLots()* OrderOpenPrice();
        }
     }

   double priceTrungBinh = priceTotal/countLot;
   for(int trade = OrdersTotal()-1; trade >= 0; trade--)
     {
      if(!OrderSelect(trade,SELECT_BY_POS,MODE_TRADES))
         continue;
      if(OrderMagicNumber() == magicNumber && OrderType() == OP_SELL && type == "SELL")
        {
         OrderModify(OrderTicket(),OrderOpenPrice(),NULL, priceTrungBinh - tpPoint* Point,0);
        }

      if(OrderMagicNumber() == magicNumber && OrderType() == OP_BUY && type == "BUY")
        {
         OrderModify(OrderTicket(),OrderOpenPrice(),NULL, priceTrungBinh + tpPoint* Point,0);
        }
     }
  }
//+------------------------------------------------------------------+
int countOrderWithHand(string type)
  {
   int countOrderBuy = 0;
   int countOrderSell = 0;
   for(int trade = OrdersTotal()-1; trade >= 0; trade--)
     {
      if(!OrderSelect(trade,SELECT_BY_POS,MODE_TRADES))
         continue;
      if(OrderMagicNumber() == 0 && OrderType() == OP_SELL)
        {
         countOrderSell = countOrderSell + 1;
        }

      if(OrderMagicNumber() == 0 && OrderType() == OP_BUY)
        {
         countOrderBuy = countOrderBuy + 1;
        }
     }
   if(type == "BUY")
     {
      return countOrderBuy;
     }
   if(type == "SELL")
     {
      return countOrderSell;
     }
   return 0;
  }
//+------------------------------------------------------------------+
