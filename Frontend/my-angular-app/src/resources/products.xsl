<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <xsl:output method="html"/>

  <xsl:template match="/">
    <html>
      <head>
        <title>Product History</title>
        <style>
          table, th, td {
            border: 1px solid black;
            border-collapse: collapse;
            padding: 8px;
          }
          th {
            background-color: #ddd;
          }
        </style>
      </head>
      <body>
        <h2>Product History</h2>
        <table>
          <tr>
            <th>Name</th>
            <th>Price</th>
            <th>Date</th>
            <th>Category</th>
          </tr>
          <xsl:for-each select="products/product">
            <tr>
              <td><xsl:value-of select="name"/></td>
              <td><xsl:value-of select="price"/></td>
              <td><xsl:value-of select="date"/></td>
              <td><xsl:value-of select="categoryName"/></td>
            </tr>
          </xsl:for-each>
        </table>
      </body>
    </html>
  </xsl:template>
</xsl:stylesheet>
