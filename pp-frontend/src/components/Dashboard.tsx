import React, { useMemo } from 'react';
import {
  Box,
  Grid,
  Paper,
  Typography,
  Card,
  CardContent,
  LinearProgress,
  Chip,
} from '@mui/material';
import {
  TrendingUp as TrendingUpIcon,
  Description as DocumentIcon,
  Warning as WarningIcon,
  CheckCircle as CheckCircleIcon,
} from '@mui/icons-material';
import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  ResponsiveContainer,
  PieChart,
  Pie,
  Cell,
  Legend,
} from 'recharts';
import { useDocuments } from '../hooks/useDocuments';
import { getDaysSinceDate } from '../utils/helpers';

const COLORS = ['#0088FE', '#00C49F', '#FFBB28', '#FF8042', '#8884D8'];

export const Dashboard: React.FC = () => {
  // Fetch a larger dataset for analytics (you might want to create a separate endpoint for this)
  const { data, isLoading } = useDocuments({ page: 0, size: 1000 });

  const metrics = useMemo(() => {
    if (!data?.content) {
      return {
        total: 0,
        fresh: 0,
        stale: 0,
        outdated: 0,
        statusBreakdown: [],
        tagBreakdown: [],
        freshnessData: [],
      };
    }

    const documents = data.content;
    const total = documents.length;

    // Calculate freshness metrics
    const fresh = documents.filter(doc => getDaysSinceDate(doc.documentLastUpdatedAt) < 30).length;
    const stale = documents.filter(doc => {
      const days = getDaysSinceDate(doc.documentLastUpdatedAt);
      return days >= 30 && days < 90;
    }).length;
    const outdated = documents.filter(doc => getDaysSinceDate(doc.documentLastUpdatedAt) >= 90).length;

    // Status breakdown
    const statusCounts: Record<string, number> = {};
    documents.forEach(doc => {
      statusCounts[doc.status] = (statusCounts[doc.status] || 0) + 1;
    });
    const statusBreakdown = Object.entries(statusCounts).map(([status, count]) => ({
      name: status,
      value: count,
      percentage: ((count / total) * 100).toFixed(1),
    }));

    // Tag breakdown (top 10)
    const tagCounts: Record<string, number> = {};
    documents.forEach(doc => {
      doc.tags.forEach(tag => {
        tagCounts[tag] = (tagCounts[tag] || 0) + 1;
      });
    });
    const tagBreakdown = Object.entries(tagCounts)
      .map(([tag, count]) => ({ name: tag, count }))
      .sort((a, b) => b.count - a.count)
      .slice(0, 10);

    // Freshness data for bar chart
    const freshnessData = [
      { name: 'Fresh (<30 days)', count: fresh, fill: '#4caf50' },
      { name: 'Stale (30-90 days)', count: stale, fill: '#ff9800' },
      { name: 'Outdated (>90 days)', count: outdated, fill: '#f44336' },
    ];

    return {
      total,
      fresh,
      stale,
      outdated,
      statusBreakdown,
      tagBreakdown,
      freshnessData,
    };
  }, [data]);

  if (isLoading) {
    return (
      <Box sx={{ p: 3 }}>
        <LinearProgress />
        <Typography sx={{ mt: 2 }}>Loading dashboard data...</Typography>
      </Box>
    );
  }

  return (
    <Box sx={{ p: 3 }}>
      <Typography variant="h4" gutterBottom sx={{ mb: 3 }}>
        PagePulse Dashboard
      </Typography>

      {/* Metrics Cards */}
      <Grid container spacing={3} sx={{ mb: 4 }}>
        <Grid item xs={12} sm={6} md={3}>
          <Card>
            <CardContent>
              <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
                <Box>
                  <Typography variant="h4" color="primary">
                    {metrics.total}
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    Total Documents
                  </Typography>
                </Box>
                <DocumentIcon color="primary" sx={{ fontSize: 40 }} />
              </Box>
            </CardContent>
          </Card>
        </Grid>

        <Grid item xs={12} sm={6} md={3}>
          <Card>
            <CardContent>
              <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
                <Box>
                  <Typography variant="h4" color="success.main">
                    {metrics.fresh}
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    Fresh Documents
                  </Typography>
                </Box>
                <CheckCircleIcon color="success" sx={{ fontSize: 40 }} />
              </Box>
            </CardContent>
          </Card>
        </Grid>

        <Grid item xs={12} sm={6} md={3}>
          <Card>
            <CardContent>
              <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
                <Box>
                  <Typography variant="h4" color="warning.main">
                    {metrics.stale}
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    Stale Documents
                  </Typography>
                </Box>
                <WarningIcon color="warning" sx={{ fontSize: 40 }} />
              </Box>
            </CardContent>
          </Card>
        </Grid>

        <Grid item xs={12} sm={6} md={3}>
          <Card>
            <CardContent>
              <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
                <Box>
                  <Typography variant="h4" color="error.main">
                    {metrics.outdated}
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    Outdated Documents
                  </Typography>
                </Box>
                <TrendingUpIcon color="error" sx={{ fontSize: 40 }} />
              </Box>
            </CardContent>
          </Card>
        </Grid>
      </Grid>

      {/* Charts */}
      <Grid container spacing={3}>
        {/* Document Freshness Chart */}
        <Grid item xs={12} md={6}>
          <Paper sx={{ p: 3 }}>
            <Typography variant="h6" gutterBottom>
              Document Freshness
            </Typography>
            <ResponsiveContainer width="100%" height={300}>
              <BarChart data={metrics.freshnessData}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="name" />
                <YAxis />
                <Tooltip />
                <Bar dataKey="count" fill="#8884d8" />
              </BarChart>
            </ResponsiveContainer>
          </Paper>
        </Grid>

        {/* Status Distribution Chart */}
        <Grid item xs={12} md={6}>
          <Paper sx={{ p: 3 }}>
            <Typography variant="h6" gutterBottom>
              Status Distribution
            </Typography>
            <ResponsiveContainer width="100%" height={300}>
              <PieChart>
                <Pie
                  data={metrics.statusBreakdown}
                  cx="50%"
                  cy="50%"
                  labelLine={false}
                  label={({ name, percentage }) => `${name} (${percentage}%)`}
                  outerRadius={80}
                  fill="#8884d8"
                  dataKey="value"
                >
                  {metrics.statusBreakdown.map((entry, index) => (
                    <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                  ))}
                </Pie>
                <Tooltip />
                <Legend />
              </PieChart>
            </ResponsiveContainer>
          </Paper>
        </Grid>

        {/* Top Tags */}
        <Grid item xs={12}>
          <Paper sx={{ p: 3 }}>
            <Typography variant="h6" gutterBottom>
              Most Common Tags
            </Typography>
            <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 1 }}>
              {metrics.tagBreakdown.map((tag, index) => (
                <Chip
                  key={tag.name}
                  label={`${tag.name} (${tag.count})`}
                  variant="outlined"
                  size="medium"
                  sx={{
                    borderColor: COLORS[index % COLORS.length],
                    color: COLORS[index % COLORS.length],
                  }}
                />
              ))}
              {metrics.tagBreakdown.length === 0 && (
                <Typography variant="body2" color="text.secondary">
                  No tags found
                </Typography>
              )}
            </Box>
          </Paper>
        </Grid>
      </Grid>
    </Box>
  );
};
