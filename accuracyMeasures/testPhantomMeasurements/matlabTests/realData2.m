%length between points
len_O_1=25.5;
len_O_2=51;
len_O_3=50;
len_O_4=59;
len_O_5=56;
len_O_6=52.5;
len_O_7=25.5;
len_1_11=39.5;
len_1_12=40;
len_7_71=18.5;
len_7_72=41;
len_11_12=24.5;
len_12_2=26.5;
len_1_2=36;
len_2_3=49.5;
len_3_4=25.5;
len_4_5=25.5;
len_5_6=46.5;
len_6_7=32;
len_71_72=26.5;
len_6_71=24.5;

%gets the real coordinates using circle-circle intersections
%       instead of polar to cartesian conversion
point_O = [0 0];
point_6 = [52.5 0]; %the positive x-axis

%point 5 is to the left of the O-6 line
point_5 = getPointFromTwoCircles(point_O,len_O_5,point_6,len_5_6,1);

%point 7 is to the right of the O-6 line
point_7 = getPointFromTwoCircles(point_O,len_O_7,point_6,len_6_7,0);

%test 5 coords
b = abs(point_5(2));
c1 = len_O_5;
c2 = len_5_6;
a1 = sqrt(c1*c1-b*b); a2 = sqrt(c2*c2-b*b); a = a1+a2;
testVal5 = a-len_O_6; %should be close to zero

%test 7 coords
b = abs(point_7(2));
c1 = len_O_7;
c2 = len_6_7;
a1 = sqrt(c1*c1-b*b); a2 = sqrt(c2*c2-b*b); a = a1+a2;
testVal7 = a-len_O_6; %should be close to zero

%point 71 is to the right of the 7-6 line
point_71 = getPointFromTwoCircles(point_7,len_7_71,point_6,len_6_71,0);

%point 72 is to the right of the 7-71 line
point_72 = getPointFromTwoCircles(point_7,len_7_72,point_71,len_71_72,0);

%point 4 is to the left of the O-5 line
point_4 = getPointFromTwoCircles(point_O,len_O_4,point_5,len_4_5,1);

%point 3 is to the left of the O-4 line
point_3 = getPointFromTwoCircles(point_O,len_O_3,point_4,len_3_4,1);

%point 2 is to the left of the O-3 line
point_2 = getPointFromTwoCircles(point_O,len_O_2,point_3,len_2_3,1);

%point 1 is to the left of the O-2 line
point_1 = getPointFromTwoCircles(point_O,len_O_1,point_2,len_1_2,1);

%point 12 is to the left of the 1-2 line
point_12 = getPointFromTwoCircles(point_1,len_1_12,point_2,len_12_2,1);

%point 11 is to the left of the 1-12 line
point_11 = getPointFromTwoCircles(point_1,len_1_11,point_12,len_11_12,1);


