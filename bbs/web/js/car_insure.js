var stageinfo = {};
function carStage(list,cb){
    var carPrice = Number(list.carPrice);
    var percent = Number(list.prepaymentPercent);
    var seatCount = Number(list.seatCount);
    //车价首付金额
    stageinfo.Prepayment = carPrice * percent;
    //	贷款额本金=购车价格 - 首付款
    stageinfo.BankLoan = carPrice - stageinfo.Prepayment;
    //月供
    if(list.prepaymentPercent==1){
        stageinfo.MonthPay = 0;
    }else{
        getMonthPay (list);
    }

    //购置税
    stageinfo.PurchaseTax = getPositive(Math.round((carPrice / 1.17) * 0.1));
    //上牌费用
    stageinfo.LicenseTax = 500;
    //车船税
    getUsageTax(list.displacement);
    //交强险
    getTrafficInsurance (seatCount);
    //第三者责任险
    getThirdInsurance(list.CommInsureCheck.thirdCheck,seatCount,list.thirdInsureClaim);
    //车辆损失
    getDamageInsurance (list.CommInsureCheck.damageCheck,carPrice,seatCount);
    //全车盗抢
    getStolenInsurance(list.CommInsureCheck.stolenCheck,carPrice,seatCount);
    //玻璃单独破碎
    getGlassInsurance (list.CommInsureCheck.glassCheck,carPrice,list.isImport);
    //自燃损失
    getCombustInsurance (list.CommInsureCheck.combustCheck,carPrice);
    //不计免赔特约
    getNoDeductibleInsurance (list.CommInsureCheck.noDeductibleCheck);
    //车上人员责任
    getPassengerInsurance (list.CommInsureCheck.passengerCheck,seatCount);
    //车身划痕
    getCarBodyInsurance (list.CommInsureCheck.carBodyCheck,carPrice,list.carBodyInsureClaim);
    //商业险总合
    stageinfo.bizfeeTotal = stageinfo.ThirdInsurance + stageinfo.DamageInsurance + stageinfo.StolenInsurance + stageinfo.GlassInsurance + stageinfo.CombustInsurance + stageinfo.NoDeductibleInsurance + stageinfo.PassengerInsurance + stageinfo.CarBodyInsurance;
    //总利息:月供*总月数-本金

    var months = Number(list.loanYears) * 12;
    stageinfo.interestTotal =  Math.round((stageinfo.MonthPay)*months - stageinfo.BankLoan);

    //首付款:首付车款+购置税+上牌费用+车船税 + 交强险 + 商业险总合;;
    stageinfo.firstPay = stageinfo.Prepayment +  stageinfo.PurchaseTax + stageinfo.LicenseTax + stageinfo.UsageTax + stageinfo.TrafficInsurance + stageinfo.bizfeeTotal;

    //预计购车总价(不含购置税)  首付款+月供*总月数-购置税
    stageinfo.buyTotal = Math.round(stageinfo.firstPay + (stageinfo.MonthPay)*months - stageinfo.PurchaseTax);
    cb(stageinfo)

}
//月供
function getMonthPay (list) {
    var bankLoan = stageinfo.BankLoan,
        loanYears = Number(list.loanYears);
    months = loanYears * 12,
        rate = 0;
    if (loanYears == 1) {
        rate = 0.0485 / 12;
    } else if (loanYears == 2) {
        rate = 0.0525 / 12;
    } else if (loanYears == 3) {
        rate = 0.0525 / 12;
    } else if (loanYears == 4) {
        rate = 0.0525 / 12;
    } else if (loanYears == 5) {
        rate = 0.0525 / 12;
    }

    stageinfo.MonthPay  = Math.round(bankLoan * ((rate * Math.pow(1 + rate, months)) / (Math.pow(1 + rate, months) - 1)));
}

//车船税
function getUsageTax (displacement) {
    if (displacement <= 1.0) {
        stageinfo.UsageTax =  300;
        return;
    } else if (displacement > 1.0 && displacement <= 1.6) {
        stageinfo.UsageTax = 420;
        return
    } else if (displacement > 1.6 && displacement <= 2.0) {
        stageinfo.UsageTax = 480;
        return
    } else if (displacement > 2.0 && displacement <= 2.5) {
        stageinfo.UsageTax = 900;
        return
    } else if (displacement > 2.5 && displacement <= 3.0) {
        stageinfo.UsageTax = 1920;
        return
    } else if (displacement > 3.0 && displacement <= 4.0) {
        stageinfo.UsageTax = 3480;
        return;
    } else if (displacement > 4.0) {
        stageinfo.UsageTax = 5280;
        return
    }
}

//交强险
function getTrafficInsurance (seatCount) {
    if (seatCount < 6) {
        stageinfo.TrafficInsurance = 950;
        return;
    }

    stageinfo.TrafficInsurance = 1100;
}

//第三者责任险
function getThirdInsurance(thirdCheck,seatCount,thirdInsureClaim) {
    //没有选中
    if (!thirdCheck) {
        stageinfo.ThirdInsurance = 0;
        return
    }
    if (seatCount < 6) {
        switch (thirdInsureClaim) {
            case 50000:
                stageinfo.ThirdInsurance = 673;//516
                return
            case 100000:
                stageinfo.ThirdInsurance = 972;//746
                return
            case 200000:
                stageinfo.ThirdInsurance = 1204;//924
                return
            case 500000:
                stageinfo.ThirdInsurance = 1631;//1252
                return
            case 1000000:
                stageinfo.ThirdInsurance = 2124;//1630
                return
            default:
                stageinfo.ThirdInsurance = 972;//746
                return
        }
    } else {
        switch (thirdInsureClaim) {
            case 50000:
                stageinfo.ThirdInsurance = 843;
                return
            case 100000:
                stageinfo.ThirdInsurance = 1186;
                return
            case 200000:
                stageinfo.ThirdInsurance = 1446;
                return
            case 500000:
                stageinfo.ThirdInsurance = 1928;
                return
            case 1000000:
                stageinfo.ThirdInsurance = 2512;
                return
            default:
                stageinfo.ThirdInsurance = 1186;
                return
        }
    }
}

//车辆损失
function getDamageInsurance (damageCheck,carPrice,seatCount) {
    //没有选中
    if (!damageCheck) {
        stageinfo.DamageInsurance = 0;
        return
    }
    var base = 566;
    if (seatCount >= 6) {
        base = 679;
    }
    stageinfo.DamageInsurance = getPositive(Math.round(base + carPrice * 0.0135));//汽车之家为0.01088
}

//全车盗抢
function getStolenInsurance (stolenCheck,carPrice,seatCount) {
    //没有选中
    if (!stolenCheck) {
        stageinfo.StolenInsurance =  0;
        return
    }
    if (seatCount >= 6) {
        stageinfo.StolenInsurance =  getPositive(Math.round(140 + carPrice * 0.0045));//汽车之家为0.00374
    } else {
        stageinfo.StolenInsurance = getPositive(Math.round(120 + carPrice * 0.0041));//汽车之家为0.004505
    }
}

//玻璃单独破碎
function getGlassInsurance (glassCheck,carPrice,isImport) {
    //没有选中
    if (!glassCheck) {
        stageinfo.GlassInsurance = 0;
        return
    }
    if (isImport == 1) {
        stageinfo.GlassInsurance =  getPositive(Math.round(carPrice * 0.0036));//汽车之家为0.0025
    } else {
        stageinfo.GlassInsurance =  getPositive(Math.round(carPrice * 0.0021));//汽车之家为0.0015
    }
}

//自燃损失
function getCombustInsurance (combustCheck,carPrice) {
    //没有选中
    if (!combustCheck) {
        stageinfo.CombustInsurance = 0;
        return
    }
    stageinfo.CombustInsurance = getPositive(Math.round(carPrice * 0.002));//0.0015
}

//不计免赔特约
function getNoDeductibleInsurance (noDeductibleCheck) {
    //没有选中
    if (!noDeductibleCheck) {
        stageinfo.NoDeductibleInsurance =  0;
        return
    }

    stageinfo.NoDeductibleInsurance = getPositive(Math.round((stageinfo.DamageInsurance + stageinfo.ThirdInsurance) * 0.15));
}

//车上人员责任
function getPassengerInsurance (passengerCheck,seatCount) {
    //没有选中
    if (!passengerCheck) {
        stageinfo.PassengerInsurance =  0;
        return
    }
    stageinfo.PassengerInsurance = getPositive(26*(seatCount-1)) + 41;
}

//车身划痕
function getCarBodyInsurance (carBodyCheck,carPrice,carBodyInsureClaim) {
    //没有选中
    if (!carBodyCheck || carPrice == 0) {
        stageinfo.CarBodyInsurance = 0;
        return
    }
    if (carBodyInsureClaim == 2000) {
        if (carPrice > 0 && carPrice <= 300000) {
            stageinfo.CarBodyInsurance = 400
        }
        if (carPrice > 300000 && carPrice <= 500000) {
            stageinfo.CarBodyInsurance = 585
        }
        if (carPrice > 500000) {
            stageinfo.CarBodyInsurance = 850
        }

    } else if (carBodyInsureClaim == 5000) {
        if (carPrice > 0 && carPrice <= 300000) {
            stageinfo.CarBodyInsurance = 570
        }
        if (carPrice > 300000 && carPrice <= 500000) {
            stageinfo.CarBodyInsurance = 900
        }
        if (carPrice > 500000) {
            stageinfo.CarBodyInsurance = 1100
        }
    } else if (carBodyInsureClaim == 10000) {
        if (carPrice > 0 && carPrice <= 300000) {
            stageinfo.CarBodyInsurance = 760
        }
        if (carPrice > 300000 && carPrice <= 500000) {
            stageinfo.CarBodyInsurance = 1170
        }
        if (carPrice > 500000) {
            stageinfo.CarBodyInsurance = 1500
        }
    } else if (carBodyInsureClaim == 20000) {
        if (carPrice > 0 && carPrice <= 300000) {
            stageinfo.CarBodyInsurance = 1140
        }
        if (carPrice > 300000 && carPrice <= 500000) {
            stageinfo.CarBodyInsurance = 1780
        }
        if (carPrice > 500000) {
            stageinfo.CarBodyInsurance = 2250
        }
    }
}
function getPositive(val) {
    if (parseFloat(val) < 0) {
        return 0;
    }

    return val;
}