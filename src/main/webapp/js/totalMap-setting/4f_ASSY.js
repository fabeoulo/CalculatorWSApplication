var pXa = -0;
var pYa = -0;

var mapInfo = {
    titleName: "M棟4樓",
    x: 755,
    y: 0
};

var titleGroup = [
    {lineName: "Pre_1", x: 0, y: 350},
    {lineName: "Pre_2", x: 0, y: 410},
    {lineName: "P_L1", x: 870, y: 160},
    {lineName: "P_L2", x: 980, y: 160},
    {lineName: "P_L3", x: 1045, y: 290},
    {lineName: "P_L4", x: 1045, y: 360},
    {lineName: "P_L5", x: 1045, y: 430}
];

var testGroup = [
    {people: 2, x: 470, y: 370}, // group 1-
    {people: 2, x: 550, y: 370}, // group 16-17
    {people: 1, x: 550, y: 330}, // group 12-13
    {people: 2, x: 470, y: 290}, // group 12-13
    {people: 2, x: 550, y: 290}, // group 12-13
    {people: 2, x: 630, y: 290}, // group 14-15
    {people: 2, x: 470, y: 250}, // group 18-19
    {people: 2, x: 550, y: 250}, // group 20-21
    {people: 2, x: 630, y: 250}, // group 22-23
    {people: 2, x: 470, y: 200}, // group 12-13
    {people: 2, x: 550, y: 200}, // group 14-15
    {people: 2, x: 630, y: 200}, // group 16-17
    {people: 6, x: 530, y: 110}, // group 7-11
    {people: 6, x: 530, y: 70}, // group 1-6
    {people: 6, x: 530, y: 5} // group 1-6
];

var babGroup = [
    {people: 7, x: 100, y: 365, lineName: "Pre_1", reverse: true},
    {people: 7, x: 100, y: 425, lineName: "Pre_2", reverse: true},
    {people: 3, x: 850, y: 230, lineName: "P_L1", reverse: true},
    {people: 3, x: 980, y: 230, lineName: "P_L2", reverse: true},
    {people: 4, x: 885, y: 310, lineName: "P_L3", reverse: true},
    {people: 3, x: 920, y: 380, lineName: "P_L4", reverse: true},
    {people: 3, x: 920, y: 440, lineName: "P_L5", reverse: true}
];

var fqcGroup = [
];

var minTestTableNo = 1;
var maxTestTableNo = 42; // no use now

var sitefloor = 4;