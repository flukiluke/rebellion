set terminal png size 800,800 enhanced
set output 'image.png'
plot 'data' using 0:1 with lines title "Quiet", 'data' using 0:2 with lines title "Active", 'data' using 0:3 with lines title "Jailed"
