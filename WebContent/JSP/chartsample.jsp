<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ include file="header.jsp"%>
<section>
<br>
<h1>Charts Supported With <%=DashboardConstants.PRODUCT_NAME %>!</h1>
<h2>Pie Chart</h2>
Pie chart should have only one dimension and one measure.
<h4>Example</h4>
Number of hosts categorized by OS type in an enterprise.  
<h4>Sample Query</h4>
SELECT OPERATINGSYSTEM, COUNT(*) FROM SYSTEM_INVENTORY GROUP BY OPERATINGSYSTEM
<h4>Sample Data</h4>
<table class="admincelltable">
	<tr>
		<th>Operating System</th>
		<th>Count</th>
	</tr>
	<tr>
		<td>Windows</td>
		<td>2</td>
	</tr>
	<tr>
		<td>Solaris</td>
		<td>2</td>
	</tr>
	<tr>
		<td>AIX</td>
		<td>2</td>
	</tr>
	<tr>
		<td>Linux</td>
		<td>4</td>
	</tr>
</table>
<h4>Sample Pie Chart</h4>
<img src="./images/pie_sample.JPG"></img>
<h2>Bar, Stacked Bar, Line, Column and Stacked Column Charts</h2>
All these chart supports up to 2 dimensions and up to 4 measures.
<h4>Example</h4>
CPU, Memory and SWAP Utilization of a node for last one hour. Here we have 3 measures CPU, Memory and Swap Utilization and two facts Node name and time stamp.
<h4>Sample Query</h4>
SELECT TIMESTAMP, HOSTNAME, CPU_UTIL, MEM_UTIL, SWAP_UTIL FROM SYSTEM_PERFORMANCE WHERE TIMESTAMP > DATE_ADD(NOW(), INTERVAL -1 HOUR) and (HOSTNAME like 'host1.mydomain.com')
<h4>Sample Data</h4>
<table class="admincelltable">
	<tr>
		<th>Host Name</th>
		<th>Time Stamp</th>
		<th>CPU Utilization</th>
		<th>MEMORY Utilization</th>
		<th>SWAP Utilization</th>
	</tr>
	<tr>
		<td>host1.mydomain.com</td>
		<td>9/3/2016 12:45</td>
		<td>66</td>
		<td>38</td>
		<td>90</td>
	</tr>
	<tr>
		<td>host1.mydomain.com</td>
		<td>9/3/2016 12:50</td>
		<td>21</td>
		<td>71</td>
		<td>90</td>
	</tr>
	<tr>
		<td>host1.mydomain.com</td>
		<td>9/3/2016 12:55</td>
		<td>76</td>
		<td>54</td>
		<td>42</td>
	</tr>
	<tr>
		<td>host1.mydomain.com</td>
		<td>9/3/2016 13:00</td>
		<td>68</td>
		<td>96</td>
		<td>75</td>
	</tr>
	<tr>
		<td>host1.mydomain.com</td>
		<td>9/3/2016 13:05</td>
		<td>58</td>
		<td>1</td>
		<td>33</td>
	</tr>
	<tr>
		<td>host1.mydomain.com</td>
		<td>9/3/2016 13:10</td>
		<td>37</td>
		<td>14</td>
		<td>61</td>
	</tr>
	<tr>
		<td>host1.mydomain.com</td>
		<td>9/3/2016 13:15</td>
		<td>1</td>
		<td>35</td>
		<td>72</td>
	</tr>
	<tr>
		<td>host1.mydomain.com</td>
		<td>9/3/2016 13:20</td>
		<td>9</td>
		<td>19</td>
		<td>68</td>
	</tr>
	<tr>
		<td>host1.mydomain.com</td>
		<td>9/3/2016 13:25</td>
		<td>57</td>
		<td>46</td>
		<td>60</td>
	</tr>
	<tr>
		<td>host1.mydomain.com</td>
		<td>9/3/2016 13:30</td>
		<td>45</td>
		<td>23</td>
		<td>78</td>
	</tr>
	<tr>
		<td>host1.mydomain.com</td>
		<td>9/3/2016 13:35</td>
		<td>41</td>
		<td>54</td>
		<td>46</td>
	</tr>
	<tr>
		<td>host1.mydomain.com</td>
		<td>9/3/2016 13:40</td>
		<td>69</td>
		<td>25</td>
		<td>16</td>
	</tr>
</table>

<h4>Sample Bar Chart</h4>
<img src="./images/bar_sample.JPG"></img>
<h4>Sample Stacked Bar Chart</h4>
<img src="./images/stack_bar_sample.JPG"></img>
<h4>Sample Line Chart</h4>
<img src="./images/line_sample.JPG"></img>
<h4>Sample Column Chart</h4>
<img src="./images/column_sample.JPG"></img>
<h4>Sample Stacked Column Chart</h4>
<img src="./images/stack_column_sample.JPG"></img>
</section>
<br>
<br>
<%@ include file="footer.jsp"%>