var pXa = -0;
var pYa = -0;

var mapInfo = {
    titleName: "M棟3樓包裝",
    x: 680,
    y: 0
};

var titleGroup = [
    //assy
//    {lineName: "LA", x: 1090, y: 50},
//    {lineName: "LB", x: 1090, y: 160},
//    {lineName: "LC-1", x: 1110, y: 340},
//    {lineName: "LC-2", x: 1110, y: 405},
//    {lineName: "LD", x: 730, y: 160},
//    {lineName: "LE", x: 735, y: 80},
//    {lineName: "LJ", x: 800, y: 305},
//    {lineName: "LK", x: 805, y: 305},
//    {lineName: "LL", x: 810, y: 305},
    //pkg
    {lineName: "PKG_L2", x: 785, y: 270},
    {lineName: "PKG_L3", x: 465, y: 405},
    {lineName: "PKG_L5", x: 465, y: 295},
    {lineName: "PKG_L6", x: 465, y: 210}
];

var testGroup = [
//    {people: 3, x: 1120, y: 335, straight: true}, // group 11-18
//    {people: 3, x: 1015, y: 420, reverse: true} // group 6-10

];

var babGroup = [
//    {people: 1, x: 985, y: 55, lineName: "LI"},
//    {people: 1, x: 930, y: 55, lineName: "LJ"},
//    {people: 1, x: 880, y: 55, lineName: "LK"},
////    {people: 4, x: 930, y: 55, lineName: "LA"},
////    {people: 4, x: 930, y: 170, lineName: "LB"},
////    {people: 3, x: 1000, y: 345, lineName: "LC-1"},
////    {people: 3, x: 1000, y: 410, lineName: "LC-2"},
////    {people: 3, x: 795, y: 170, lineName: "LD"},
    {people: 4, x: 805, y: 350, lineName: "PKG_L2", straight: true},
    {people: 3, x: 615, y: 415, lineName: "PKG_L3", reverse: true},
    {people: 3, x: 615, y: 315, lineName: "PKG_L5", reverse: true},
    {people: 4, x: 485, y: 75, lineName: "PKG_L6", straight: true}
];

var fqcGroup = [
//    {people: 1, x: 300, y: 215, lineName: "FQC_3"},
//    {people: 1, x: 300, y: 255, lineName: "FQC_4"},
//    {people: 1, x: 300, y: 300, lineName: "FQC_5"}
];

var minTestTableNo = 31;
var maxTestTableNo = 36; // no use now
var omitTestTableNo = [];
var nextTestTableNo;

var sitefloor = 3; //for test table